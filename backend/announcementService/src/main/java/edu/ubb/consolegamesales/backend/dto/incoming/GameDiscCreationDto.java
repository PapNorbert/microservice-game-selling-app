package edu.ubb.consolegamesales.backend.dto.incoming;

import edu.ubb.consolegamesales.backend.model.GameDiscType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GameDiscCreationDto {
    @NotNull @NotBlank
    private String name;
    @NotNull
    private GameDiscType type;
    @NotNull @Positive
    private Integer gameYear;
}
