package ru.inrtu.backend.service;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.inrtu.backend.customException.ScheduleAlreadyExistException;
import ru.inrtu.backend.entity.Schedule;
import ru.inrtu.backend.repository.ScheduleRepository;

import java.util.List;

@Service
public class ScheduleService {

    private static Logger logger = LoggerFactory.getLogger(ScheduleService.class);
    private ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository){
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule create(Schedule schedule) throws ScheduleAlreadyExistException{
        Schedule existingSchedule = scheduleRepository.findExistingInDB(
                schedule.getStartEventDate(), schedule.getEndEventDate(), schedule.getActivity().getId());
        if (existingSchedule == null){
            logger.info("Existing schedule is NULL");
            return scheduleRepository.saveAndFlush(schedule);
        }else{
            logger.info("Existing schedule is NOT NULL");
            throw new ScheduleAlreadyExistException();
        }
    }

    public Schedule update(Schedule schedule){
        return scheduleRepository.save(schedule);
    }

    public void delete(Long id){
        scheduleRepository.deleteById(id);
    }

    public Schedule get(Long id){
        return scheduleRepository.findById(id).orElseThrow();
    }

    public List<Schedule> getAll(){
        return scheduleRepository.findAll();
    }

}
