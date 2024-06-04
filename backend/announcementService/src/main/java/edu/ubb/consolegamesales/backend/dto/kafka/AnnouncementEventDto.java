package edu.ubb.consolegamesales.backend.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementEventDto {
    private Long announcementId;
    private String event;
    private Long changedByUserId;
}
