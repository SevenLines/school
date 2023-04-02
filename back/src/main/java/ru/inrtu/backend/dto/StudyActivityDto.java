package ru.inrtu.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudyActivityDto {

    private Long id;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private Integer participationPoint;
    private Integer maxParticipants;
    //Enum ActivityType
    private String activityType;
    private Long[] trajectoriesId;
}
