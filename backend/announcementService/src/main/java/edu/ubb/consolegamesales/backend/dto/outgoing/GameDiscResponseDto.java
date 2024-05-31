package edu.ubb.consolegamesales.backend.dto.outgoing;

import edu.ubb.consolegamesales.backend.model.GameDiscType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class GameDiscResponseDto {
    @NotNull
    @Positive
    private Long gameDiscId;
    @NotNull
    private String name;
    @NotNull
    private GameDiscType type;
    @NotNull
    @Positive
    private Integer gameYear;

}
