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
import ru.inrtu.backend.entity.ActivityHistory;
import ru.inrtu.backend.entity.StudyActivity;
import ru.inrtu.backend.enums.ActivityHistoryRecordType;
import ru.inrtu.backend.repository.ActivityHistoryRepository;
import ru.inrtu.backend.service.StudyActivityService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.json.Json;

/**
 * Класс, для тестирования контроллера мероприятий.
 *
 * @author Kamron Boturkhonov
 * @since 2023.03.28
 */
public class ActivityControllerTest extends AbstractIntegrationTestBase {

    /**
     * Наименования тестовых мероприятий.
     */
    private static final String[] ACTIVITY_NAMES =
            new String[] { "Научный прорыв", "Лаборатория творчества", "Педагогические каникулы" };

    private static final String ACTIVITY_NAMES_PATH = "$[*].name";

    /**
     * Сервис, который содержит логику по работе с сущностями StudyActivity.
     */
    @Autowired
    private StudyActivityService studyActivityService;

    @Autowired
    private ActivityHistoryRepository activityHistoryRepository;

    @Test
    public void testGetAllActivities() throws Exception {
        final String result =
                mockMvc.perform(MockMvcRequestBuilders.get("/api/activities").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8);

        final List<String> activityNames = JsonPath.read(result, ACTIVITY_NAMES_PATH);

        assertThat(activityNames.size()).isEqualTo(3);
        assertThat(activityNames).containsExactlyInAnyOrder(ACTIVITY_NAMES);

    }

    @Test
    public void testGetOneActivity() throws Exception {
        final String result = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/activities/1000").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        final List<String> activityName = JsonPath.read(result, ".name");
        assertThat(activityName.size()).isEqualTo(1);
        assertThat(activityName.get(0)).isEqualTo("Научный прорыв");
    }

    @Test
    public void testRegisterActivity() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/activities/register")
                .content(Json.createObjectBuilder()
                        .add(ActivityAppointmentDto.Property.SCHOOLCHILD_ID, "1002")
                        .add(ActivityAppointmentDto.Property.ACTIVITY_ID, "1001")
                        .build()
                        .toString())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
        cleaners.add(() -> {
            ActivityHistory history = activityHistoryRepository
                    .findExistingInDB(ActivityHistoryRecordType.REGISTERED.getName(), 1001L, 1002L);
            activityHistoryRepository.deleteById(history.getId());
        });
    }

    @Test
    public void testAttendActivity() throws Exception {

        studyActivityService.register(new ActivityAppointmentDto().setSchoolchildId(1002L).setActivityId(1001L));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/activities/appointment")
                .content(Json.createObjectBuilder()
                        .add(ActivityAppointmentDto.Property.SCHOOLCHILD_ID, "1002")
                        .add(ActivityAppointmentDto.Property.ACTIVITY_ID, "1001")
                        .build()
                        .toString())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());

        cleaners.add(() -> {
            studyActivityService
                    .deleteAppointment(new ActivityAppointmentDto().setSchoolchildId(1002L).setActivityId(1001L));
            ActivityHistory history = activityHistoryRepository
                    .findExistingInDB(ActivityHistoryRecordType.REGISTERED.getName(), 1001L, 1002L);
            activityHistoryRepository.deleteById(history.getId());
        });
    }

    @Test
    public void testDeleteAppointment() throws Exception {
        final ActivityAppointmentDto appointmentDto =
                new ActivityAppointmentDto().setSchoolchildId(1002L).setActivityId(1001L);
        studyActivityService.register(appointmentDto);
        studyActivityService.appointment(appointmentDto);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/activities/appointment")
                .content(Json.createObjectBuilder()
                        .add(ActivityAppointmentDto.Property.SCHOOLCHILD_ID, "1002")
                        .add(ActivityAppointmentDto.Property.ACTIVITY_ID, "1001")
                        .build()
                        .toString())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
        cleaners.add(() -> {
            ActivityHistory history = activityHistoryRepository
                    .findExistingInDB(ActivityHistoryRecordType.REGISTERED.getName(), 1001L, 1002L);
            activityHistoryRepository.deleteById(history.getId());
        });
    }

    @Test
    public void testCreateActivity() throws Exception {
        final String result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/activities/create")
                        .content(Json.createObjectBuilder()
                                .add(StudyActivity.Property.NAME, "test name")
                                .add(StudyActivity.Property.DESCRIPTION, "description")
                                .build()
                                .toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        final List<String> activityNames = JsonPath.read(result, ".name");
        assertThat(activityNames.size()).isEqualTo(1);
        assertThat(activityNames.get(0)).isEqualTo("test name");

        final List<Integer> activityId = JsonPath.read(result, ".id");
        cleaners.add(() -> studyActivityService.delete(Long.valueOf(activityId.get(0))));
    }

    @Test
    public void testUpdateActivity() throws Exception {

        final StudyActivity studyActivity = studyActivityService.create(new StudyActivity().setName("Test name"));

        final String result = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/activities")
                        .content(Json.createObjectBuilder()
                                .add("id", studyActivity.getId())
                                .add(StudyActivity.Property.DESCRIPTION, "Test description")
                                .build()
                                .toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        final List<String> updatedDescription = JsonPath.read(result, ".description");
        assertThat(updatedDescription.size()).isEqualTo(1);
        assertThat(updatedDescription.get(0)).isEqualTo("Test description");

        cleaners.add(() -> studyActivityService.delete(studyActivity.getId()));
    }

    @Test
    public void testDeleteActivity() throws Exception {
        final StudyActivity studyActivity = studyActivityService.create(new StudyActivity().setName("Test name"));
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/activities/" + studyActivity.getId())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
    }
}
