package ru.inrtu.backend.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Access;

@Data
@Accessors(chain = true)
@ToString
public class SchoolchildrenStatusActivityDto {

    private Long id;
    private String name;
    private String surname;
    private String fatherName;
    private String educationalOrganization;
    private String educationalClass;
    private String email;
    private String phoneNumber;
    private boolean attendActivity;
}
