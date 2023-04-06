package ru.inrtu.backend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.inrtu.backend.AbstractIntegrationTestBase;
import ru.inrtu.backend.dto.ActivityAppointmentDto;
import ru.inrtu.backend.service.ActivityHistoryService;
import ru.inrtu.backend.service.StudyActivityService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, для тестирования контроллера школьников.
 *
 * @author Kamron Boturkhonov
 * @since 2023.04.06
 */
public class SchoolchildrenControllerTest extends AbstractIntegrationTestBase {

    @Autowired
    private StudyActivityService studyActivityService;

    @Autowired
    private ActivityHistoryService activityHistoryService;

    @Test
    public void testGetRegisteredSchoolchildren() throws Exception {
        final List<Long> idsToRemove = new ArrayList<>(List.of(
                studyActivityService.register(new ActivityAppointmentDto().setSchoolchildId(1002L).setActivityId(1000L))
                        .getId(),
                studyActivityService.register(new ActivityAppointmentDto().setSchoolchildId(1003L).setActivityId(1000L))
                        .getId(),
                studyActivityService.register(new ActivityAppointmentDto().setSchoolchildId(1004L).setActivityId(1000L))
                        .getId()));

        cleaners.add(() -> idsToRemove.forEach(id -> activityHistoryService.delete(id)));

        final String result = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/schoolchildren/registered")
                        .param("activityId", "1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        final List<Integer> resultIds = JsonPath.read(result, "$[*].id");
        assertThat(resultIds.size()).isEqualTo(3);
        assertThat(resultIds).containsExactlyInAnyOrder(1002, 1003, 1004);
    }

    @Test
    public void testGetAppointmentSchoolchildren() throws Exception {

        final List<Long> idsToRemove = new ArrayList<>(List.of(
                studyActivityService.register(new ActivityAppointmentDto().setSchoolchildId(1002L).setActivityId(1000L))
                        .getId(),
                studyActivityService.register(new ActivityAppointmentDto().setSchoolchildId(1003L).setActivityId(1000L))
                        .getId(),
                studyActivityService.register(new ActivityAppointmentDto().setSchoolchildId(1004L).setActivityId(1000L))
                        .getId()));

        idsToRemove.addAll(List.of(
                studyActivityService
                        .appointment(new ActivityAppointmentDto().setSchoolchildId(1002L).setActivityId(1000L))
                        .getId(),
                studyActivityService
                        .appointment(new ActivityAppointmentDto().setSchoolchildId(1003L).setActivityId(1000L))
                        .getId(),
                studyActivityService
                        .appointment(new ActivityAppointmentDto().setSchoolchildId(1004L).setActivityId(1000L))
                        .getId()));

        cleaners.add(() -> idsToRemove.forEach(id -> activityHistoryService.delete(id)));

        final String result = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/schoolchildren/appointment")
                        .param("activityId", "1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        final List<Integer> resultIds = JsonPath.read(result, "$[*].id");
        assertThat(resultIds.size()).isEqualTo(3);
        assertThat(resultIds).containsExactlyInAnyOrder(1002, 1003, 1004);
    }

    @Test
    public void testGetSchoolchildren() throws Exception {
        final String result = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/schoolchildren").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        final List<String> userNames = JsonPath.read(result, "$[*].name");
        assertThat(userNames).contains("Админ", "Алексей", "Андрей", "Дмитрий", "Антон", "Михаил");
    }

}
