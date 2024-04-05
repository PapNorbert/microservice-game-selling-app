package edu.ubb.consolegamesales.backend.controller.dto.outgoing;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.util.Date;

@Data
public class UserResponseDto {
    @NotNull @Positive
    private Long entityId;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private Date registrationDate;
}
