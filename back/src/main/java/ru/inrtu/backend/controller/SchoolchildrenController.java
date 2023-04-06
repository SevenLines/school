package ru.inrtu.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.inrtu.backend.dto.SchoolchildrenStatusActivityDto;
import ru.inrtu.backend.entity.Schoolchild;
import ru.inrtu.backend.service.SchoolchildService;

import java.security.Principal;
import java.util.List;

/**
 * Класс, в котором обрабатываются запросы для API методов, связанных сО школьниками(Schoolchild)
 */
@Tag(name = "SchoolchildrenController", description = "Контроллер для работы со школьниками")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schoolchildren")
public class SchoolchildrenController {

    private final SchoolchildService schoolchildService;

    @Operation(summary = "Получить список школьников, которые зарегистрировались на мероприятие.")
    @RequestMapping(value = "/registered", method = RequestMethod.GET)
    public ResponseEntity<List<SchoolchildrenStatusActivityDto>> getRegisteredSchoolchildren(@Parameter(
            description = "Идентификатор мероприятия для которого нужно получить зарегистрировавшихся школьников") @RequestParam Long activityId,
            Principal principal) {
        if (!schoolchildService.findUserByEmail(principal.getName()).isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(schoolchildService.getRegisteredSchoolchildren(activityId));
    }

    @Operation(summary = "Получить список школьников, которые посетили мероприятие.")
    @RequestMapping(value = "/appointment", method = RequestMethod.GET)
    public ResponseEntity<List<SchoolchildrenStatusActivityDto>> getAppointmentSchoolchildren(@Parameter(
            description = "Идентификатор мероприятия для которого нужно получить школьников, посетивших мероприятие") @RequestParam Long activityId,
            Principal principal) {
        if (!schoolchildService.findUserByEmail(principal.getName()).isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(schoolchildService.getAttendedSchoolchildren(activityId));
    }

    @Operation(summary = "Получить список всех школьников")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Schoolchild>> getSchoolchildren(Principal principal) {
        if (!schoolchildService.findUserByEmail(principal.getName()).isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(schoolchildService.getAll());
    }
}
