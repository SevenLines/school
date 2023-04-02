package ru.inrtu.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.inrtu.backend.customException.StudyActivityAlreadyExistException;
import ru.inrtu.backend.dto.ActivityAppointmentDto;
import ru.inrtu.backend.entity.Schoolchild;
import ru.inrtu.backend.entity.StudyActivity;
import ru.inrtu.backend.entity.ActivityHistory;
import ru.inrtu.backend.enums.ActivityHistoryRecordType;
import ru.inrtu.backend.repository.ActivityHistoryRepository;
import ru.inrtu.backend.repository.SchoolchildRepository;
import ru.inrtu.backend.repository.StudyActivityRepository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Класс, в котором содержится логика по работе с сущностями "Активность для школьника"(мероприятия)
 */
@Service
public class StudyActivityService {

    private static final Logger logger = LoggerFactory.getLogger(StudyActivityService.class);
    private final StudyActivityRepository studyActivityRepository;
    private final ActivityHistoryRepository activityHistoryRepository;
    private final SchoolchildRepository schoolchildRepository;

    @Autowired
    public StudyActivityService(StudyActivityRepository studyActivityRepository,
                                ActivityHistoryRepository activityHistoryRepository,
                                SchoolchildRepository schoolchildRepository){
        this.studyActivityRepository = studyActivityRepository;
        this.activityHistoryRepository = activityHistoryRepository;
        this.schoolchildRepository = schoolchildRepository;
    }

    public StudyActivity get(Long id){
        return studyActivityRepository.findById(id).orElseThrow();
    }

    public List<StudyActivity> getAll(){
        return studyActivityRepository.findAll();
    }

    public StudyActivity update(StudyActivity studyActivity){
        StudyActivity activityFromDB = studyActivityRepository.findById(studyActivity.getId()).orElseThrow(() ->
                 new NoSuchElementException("Мероприятие с таким id не найдено!"));
        activityFromDB
                .setName(studyActivity.getName())
                .setDescription(studyActivity.getDescription())
                .setStartDate(studyActivity.getStartDate())
                .setEndDate(studyActivity.getEndDate())
                .setParticipationPoint(studyActivity.getParticipationPoint())
                .setMaxParticipants(studyActivity.getMaxParticipants())
                .setActivityType(studyActivity.getActivityType())
                .setTrajectories(studyActivity.getTrajectories());
        return studyActivityRepository.saveAndFlush(activityFromDB);
    }

    /**
     * Создание нового мероприятия
     */
    public StudyActivity create(StudyActivity studyActivity) throws StudyActivityAlreadyExistException{
        StudyActivity existingStudyActivity = studyActivityRepository.findExistingInDB(studyActivity.getName(), studyActivity.getStartDate());
        if (existingStudyActivity == null){
            return studyActivityRepository.saveAndFlush(studyActivity);
        }else{
            throw new StudyActivityAlreadyExistException("Активность с таким именем и датой старта уже существует!");
        }
    }

    /**
     * Регистрация школьника на мероприятие
     */
    public ActivityHistory register(ActivityAppointmentDto registerDto){
        StudyActivity studyActivity = studyActivityRepository.findById(registerDto.getActivityId()).get();
        Schoolchild schoolchild = schoolchildRepository.findById(registerDto.getSchoolchildId()).get();
        ActivityHistory existedHistoryRecord = activityHistoryRepository.findExistingInDB(ActivityHistoryRecordType.REGISTERED.getName(), registerDto.getActivityId(), registerDto.getSchoolchildId());
        if (existedHistoryRecord != null){
            throw new IllegalArgumentException("В базе данных уже существует отметка о том, что школьник с id=" + registerDto.getSchoolchildId() + " зарегистрировался на мероприятие с id=" + registerDto.getActivityId());
        }
        ActivityHistory activityHistory = new ActivityHistory()
                .setActivity(studyActivity)
                .setSchoolchild(schoolchild)
                .setRecordCreateDate(ZonedDateTime.now().toString())
                .setHistoryRecordType(ActivityHistoryRecordType.REGISTERED.getName());
        return activityHistoryRepository.save(activityHistory);
    }

    /**
     * Отметка о том, что школьник посетил мероприятие
     */
    public ActivityHistory appointment(ActivityAppointmentDto appointmentDto){
        logger.info("StudyActivityService AppointmentDto: " + appointmentDto);
        StudyActivity attendedActivity = studyActivityRepository.findById(appointmentDto.getActivityId()).orElseThrow(() -> new NoSuchElementException("Мероприятие с таким activityId не найдено"));
        logger.info("StudyActivityService StudyActivity attendedActivity: " + attendedActivity);
        Schoolchild schoolchild = schoolchildRepository.findById(appointmentDto.getSchoolchildId()).orElseThrow(() -> new NoSuchElementException("Школьник с таким schoolchildId не найден"));
        logger.info("StudyActivityService schoolchild: " + schoolchild);
        ActivityHistory attendedFormDb = activityHistoryRepository.checkExist(appointmentDto.getSchoolchildId(), appointmentDto.getActivityId());
        if (attendedFormDb != null){
            throw new IllegalArgumentException(String.format("В базе данных уже существует отметка о том, что школьник с id=%s посетил мероприятие с id=%s", appointmentDto.getSchoolchildId(), appointmentDto.getActivityId()));
        }
        ActivityHistory attendHistoryRecord = new ActivityHistory()
                .setActivity(attendedActivity)
                .setSchoolchild(schoolchild)
                .setHistoryRecordType(ActivityHistoryRecordType.ATTENDED.getName())
                .setRecordCreateDate(LocalDateTime.now().toString());
        logger.info("StudyActivityService attendedHistoryRecord: " + attendHistoryRecord);
        return activityHistoryRepository.save(attendHistoryRecord);
    }

    /**
     * Метод, для удаления отметки о посещении мероприятия пользователем
     */
    public void deleteAppointment(ActivityAppointmentDto deleteAppointmentDto){
        ActivityHistory activityHistory = activityHistoryRepository.checkExist(deleteAppointmentDto.getSchoolchildId(),
                deleteAppointmentDto.getActivityId());
        if (activityHistory == null){
            throw new IllegalArgumentException(String.format("В базе данных нет отметки о посещении мероприятия " +
                    "с id=%s; для пользователя с id=%s", deleteAppointmentDto.getActivityId(), deleteAppointmentDto.getSchoolchildId()));
        }
        activityHistoryRepository.deleteById(activityHistory.getId());
        logger.info("ActivityHistory deleted success id=" + activityHistory.getId());
    }

    /**
     * Удаление мероприятия по переданному id
     */
    public void delete(Long id){
        studyActivityRepository.deleteById(id);
    }
}
