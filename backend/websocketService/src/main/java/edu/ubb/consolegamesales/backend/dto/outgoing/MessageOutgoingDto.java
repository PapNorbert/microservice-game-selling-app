package edu.ubb.consolegamesales.backend.dto.outgoing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class MessageOutgoingDto {
    @NotNull
    @Positive
    private Long entityId;
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
    private Date sentTime;
}
