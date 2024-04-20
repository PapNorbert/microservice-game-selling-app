package edu.ubb.consolegamesales.backend.controller.dto.outgoing;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreatedObjectDto {
    @NotNull  @Positive
    private Long entityId;
}
