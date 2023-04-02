package ru.inrtu.backend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.springframework.http.MediaType;
import ru.inrtu.backend.AbstractIntegrationTestBase;

import java.nio.charset.StandardCharsets;
import java.util.List;

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

    @Test
    public void testGetAllActivities() throws Exception {
        final String result = mockMvc.perform(get("/api/activities").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        System.out.println(result);

        final List<String> activityNames = JsonPath.read(result, ACTIVITY_NAMES_PATH);

        assertThat(activityNames.size()).isEqualTo(3);
        assertThat(activityNames).containsExactlyInAnyOrder(ACTIVITY_NAMES);

    }

}
