package ru.inrtu.backend.dto;

import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String password;

    private String name;

    private String secondName;

    private String lastName;

    private String city;

    private String schoolClass;

    private String phoneNumber;

    private String email;

    private String token;
}
