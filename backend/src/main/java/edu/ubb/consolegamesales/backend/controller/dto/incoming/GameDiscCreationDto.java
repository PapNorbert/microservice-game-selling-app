package edu.ubb.consolegamesales.backend.controller.dto.incoming;

import edu.ubb.consolegamesales.backend.model.GameDiscType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GameDiscCreationDto {
    @NotNull
    private String name;
    @NotNull
    private GameDiscType type;
    @NotNull
    private Long userId;
    @NotNull @Positive
    private Integer gameYear;
}
