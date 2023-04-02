package ru.inrtu.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Карточка DTO запроса для отметки факта о том, что школьник посетил мероприятие
 */
@Data
@Accessors(chain = true)
@ToString
public class ActivityAppointmentDto {

    @Schema(description = "Идентификатор школьника которого нужно отметить на мероприятии")
    private Long schoolchildId;

    @Schema(description = "Идентификатор мероприятия, на котором нужно отметить присутствие школьника")
    private Long activityId;
}
