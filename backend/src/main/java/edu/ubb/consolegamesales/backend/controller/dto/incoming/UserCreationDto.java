package edu.ubb.consolegamesales.backend.controller.dto.incoming;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCreationDto {
    @NotNull
    private String username;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String password;
    @NotNull
    private String address;
}
