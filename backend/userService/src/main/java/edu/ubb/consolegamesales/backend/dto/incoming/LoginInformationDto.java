package edu.ubb.consolegamesales.backend.dto.incoming;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginInformationDto {
    @NotNull @NotBlank
    private String username;
    @NotNull @NotBlank
    private String password;
}
