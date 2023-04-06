package ru.inrtu.backend.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SchoolchildDto {

    public static final class Property {

        public static final String NAME = "name";

        public static final String EMAIL = "email";

        public static final String PASSWORD = "password";
    }

    private Long id;

    private String name;

    private String surname;

    private String fatherName;

    private String educationalOrganization;

    private String educationalClass;

    private String email;

    private String phoneNumber;

    private String password;

    private String token;
}
