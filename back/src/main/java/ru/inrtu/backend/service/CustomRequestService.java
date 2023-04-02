package ru.inrtu.backend.service;

import org.springframework.stereotype.Service;
import ru.inrtu.backend.customException.ActivityHistoryAlreadyExistException;
import ru.inrtu.backend.customException.TrajectoryNotFoundException;
import ru.inrtu.backend.entity.ActivityHistory;
import ru.inrtu.backend.entity.Schoolchild;
import ru.inrtu.backend.entity.StudyActivity;
import ru.inrtu.backend.entity.Trajectory;
import ru.inrtu.backend.enums.ActivityHistoryRecordType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomRequestService {

    private TrajectoryService trajectoryService;
    private ActivityHistoryService activityHistoryService;
    private StudyActivityService studyActivityService;

    public CustomRequestService(TrajectoryService trajectoryService,
                                StudyActivityService studyActivityService,
                                ActivityHistoryService activityHistoryService){
        this.trajectoryService = trajectoryService;
        this.studyActivityService = studyActivityService;
        this.activityHistoryService = activityHistoryService;
    }

    //Получить все активности для указанной траектории
    public List<StudyActivity> getAllActivitiesForTrajectory(Long trajectoryId) throws TrajectoryNotFoundException {
        Trajectory trajectoryForFiltering = trajectoryService.get(trajectoryId);
        List<StudyActivity>allActivities = studyActivityService.getAll();
        List<StudyActivity>activitiesForTrajectory = new ArrayList<>();
        activitiesForTrajectory = allActivities.stream()
                .filter(a -> a.getTrajectories().contains(trajectoryForFiltering))
                .collect(Collectors.toList());
        return activitiesForTrajectory;
    }

    //Записать школьника на мероприятие
    public ActivityHistory registerSchoolchildForActivity(Schoolchild schoolchild, StudyActivity studyActivity) throws ParseException, ActivityHistoryAlreadyExistException {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(new Date().toString()).toString();
        ActivityHistory activityHistory = new ActivityHistory(schoolchild, studyActivity,
                ActivityHistoryRecordType.REGISTERED.getName(), currentDate);
        activityHistoryService.create(activityHistory);
        return activityHistory;
    }

}
