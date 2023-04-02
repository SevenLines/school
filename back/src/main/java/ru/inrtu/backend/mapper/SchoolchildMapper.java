package ru.inrtu.backend.mapper;

import org.springframework.stereotype.Component;
import ru.inrtu.backend.dto.SchoolchildDto;
import ru.inrtu.backend.entity.Schoolchild;

@Component
public class SchoolchildMapper {

    public Schoolchild toEntity(SchoolchildDto schoolchildDto){
        return new Schoolchild()
                .setId(schoolchildDto.getId())
                .setName(schoolchildDto.getName())
                .setSurname(schoolchildDto.getSurname())
                .setFatherName(schoolchildDto.getFatherName())
                .setEmail(schoolchildDto.getEmail())
                .setEducationalClass(schoolchildDto.getEducationalClass())
                .setEducationalOrganization(schoolchildDto.getEducationalOrganization())
                .setPhoneNumber(schoolchildDto.getPhoneNumber())
                .setPassword(schoolchildDto.getPassword());
    }

    public SchoolchildDto toDto(Schoolchild schoolchild){
        return new SchoolchildDto()
                .setId(schoolchild.getId())
                .setName(schoolchild.getName())
                .setSurname(schoolchild.getSurname())
                .setFatherName(schoolchild.getFatherName())
                .setEmail(schoolchild.getEmail())
                .setEducationalClass(schoolchild.getEducationalClass())
                .setEducationalOrganization(schoolchild.getEducationalOrganization())
                .setPhoneNumber(schoolchild.getPhoneNumber())
                .setPassword(schoolchild.getPassword());
    }
}
