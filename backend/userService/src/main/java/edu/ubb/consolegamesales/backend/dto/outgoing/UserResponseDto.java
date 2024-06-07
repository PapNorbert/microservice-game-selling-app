package edu.ubb.consolegamesales.backend.dto.outgoing;

import lombok.Data;
import java.util.Date;

@Data
public class UserResponseDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private Date registrationDate;
    private String username;
}
