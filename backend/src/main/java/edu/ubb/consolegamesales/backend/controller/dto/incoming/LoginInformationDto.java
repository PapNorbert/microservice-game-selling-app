package edu.ubb.consolegamesales.backend.controller.dto.incoming;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginInformationDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
