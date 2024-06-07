package edu.ubb.consolegamesales.backend.dto.incoming;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AnnouncementsSavesCreationDto {
    @NotNull
    @Positive
    private Long userId;
    @NotNull
    @Positive
    private Long announcementId;
}
