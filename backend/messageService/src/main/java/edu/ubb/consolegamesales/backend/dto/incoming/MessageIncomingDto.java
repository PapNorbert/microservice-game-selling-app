package edu.ubb.consolegamesales.backend.dto.incoming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MessageIncomingDto {
    @NotNull
    @Positive
    private Long senderId;
    @NotBlank
    private String senderUsername;
    @NotNull
    @Positive
    private Long receiverId;
    @NotBlank
    private String data;
}
