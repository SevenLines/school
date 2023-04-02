package ru.inrtu.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.inrtu.backend.dto.ActivityAppointmentDto;
import ru.inrtu.backend.entity.ActivityHistory;
import ru.inrtu.backend.entity.StudyActivity;
import ru.inrtu.backend.service.SchoolchildService;
import ru.inrtu.backend.service.StudyActivityService;

import java.security.Principal;
import java.util.List;

/**
 * Класс, в котором обрабатываются запросы для API методов, связанных с мероприятиями(StudyActivity)
 */
@Tag(name = "ActivityController", description = "Контроллер для работы с мероприятиями")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    /**
     * Сервис, который содержит логику по работе с сущностями StudyActivity
     */
    private final StudyActivityService studyActivityService;

    /**
     * Сервис, который содержит логику по работе с сущностями Schoolchild
     */
    private final SchoolchildService schoolchildService;

    @Operation(summary = "Получить список всех мероприятий")
    @RequestMapping(method = RequestMethod.GET)

    public ResponseEntity<List<StudyActivity>> getAllActivities() {
        logger.info("Received GET /api/activities request");
        return ResponseEntity.ok(studyActivityService.getAll());
    }

    @Operation(summary = "Получить мероприятие по id")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<StudyActivity> getOneActivity(@PathVariable Long id) {
        return ResponseEntity.ok(studyActivityService.get(id));
    }

    @Operation(summary = "Зарегистрировать школьника на мероприятие")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerActivity(@RequestBody ActivityAppointmentDto registerDto) {
        logger.info("Получен запрос /api/activities/register:  registerInfo=" + registerDto);
        try {
            return ResponseEntity.ok(studyActivityService.register(registerDto));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }

    @Operation(summary = "Отметить факт, что школьник посетил мероприятие")
    @RequestMapping(value = "/appointment", method = RequestMethod.POST)
    public ResponseEntity<?> attendActivity(
            @Parameter(description = "Информация для отметки посещения мероприятия школьником. Объект содержит " +
                    "идентификаторы мероприятия, и школьника.")
            @RequestBody ActivityAppointmentDto appointmentDto,
            Principal principal) {
        logger.info("Received AppointmentDto: " + appointmentDto);
        try {
            if (!schoolchildService.findUserByEmail(principal.getName()).isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(studyActivityService.appointment(appointmentDto));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }

    @Operation(summary = "Удалить отметку о посещении мероприятия школьником")
    @RequestMapping(value = "/appointment", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAppointment(
            @Parameter(description = "Информация для удаления отметки посещения мероприятия школьником. Объект содержит " +
                    "идентификаторы мероприятия, и школьника.")
            @RequestBody ActivityAppointmentDto deleteAppointmentDto){
        logger.info("Received request DELETE /appointment");
        try{
            studyActivityService.deleteAppointment(deleteAppointmentDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }

    @Operation(summary = "Создать мероприятие")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?>
            createActivity(@Parameter(
                    description = "Объект для мероприятия, которое нужно создать") @RequestBody StudyActivity activity,
                    Principal principal) {
        if (!schoolchildService.findUserByEmail(principal.getName()).isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            return ResponseEntity.ok(studyActivityService.create(activity));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }

    @Operation(summary = "Обновить информацию о мероприятии")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateActivity(@RequestBody StudyActivity activity, Principal principal) {
        try {
            if (!schoolchildService.findUserByEmail(principal.getName()).isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(studyActivityService.update(activity));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }

    @Operation(summary = "Удаление мероприятия")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?>deleteActivity(@PathVariable Long id){
        try {
            studyActivityService.delete(id);
            return ResponseEntity.ok().build();
        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }
}
