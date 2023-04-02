package ru.inrtu.backend.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Access;

@Data
@Accessors(chain = true)
public class SchoolchildDto {

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
