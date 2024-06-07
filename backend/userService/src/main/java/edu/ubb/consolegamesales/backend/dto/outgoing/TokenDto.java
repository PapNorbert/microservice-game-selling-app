package edu.ubb.consolegamesales.backend.dto.outgoing;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {
    @NotNull
    private String token;
}
