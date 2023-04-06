package ru.inrtu.backend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.inrtu.backend.AbstractIntegrationTestBase;
import ru.inrtu.backend.dto.SchoolchildDto;
import ru.inrtu.backend.entity.Schoolchild;
import ru.inrtu.backend.service.SchoolchildService;

import javax.json.Json;

/**
 * Тестирование регистрации/входа в платформу.
 *
 * @author Kamron Boturkhonov
 * @since 2023.04.06
 */
public class AuthenticationControllerTest extends AbstractIntegrationTestBase {

    @Autowired
    private SchoolchildService schoolchildService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void assignActiveUser(final String login) {
        // для тестов этого класса не устанавливаем пользователя
    }

    @Test
    public void testRegistration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                .content(Json.createObjectBuilder()
                        .add(SchoolchildDto.Property.NAME, "Test User")
                        .add(SchoolchildDto.Property.EMAIL, "test@test.ru")
                        .add(SchoolchildDto.Property.PASSWORD, "123")
                        .build()
                        .toString())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());

        final Schoolchild savedUser = schoolchildService.findUserByEmail("test@test.ru");

        assertThat(savedUser.getName()).isEqualTo("Test User");

        cleaners.add(() -> schoolchildService.deleteUser(savedUser.getId()));
    }

    @Test
    public void testLogin() throws Exception {
        schoolchildService.saveUser(new Schoolchild().setEmail("test@test.ru").setPassword("123"),
                bCryptPasswordEncoder);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                .content(Json.createObjectBuilder()
                        .add(SchoolchildDto.Property.EMAIL, "test@test.ru")
                        .add(SchoolchildDto.Property.PASSWORD, "123")
                        .build()
                        .toString())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());

        final Schoolchild savedUser = schoolchildService.findUserByEmail("test@test.ru");
        cleaners.add(() -> schoolchildService.deleteUser(savedUser.getId()));
    }
}
