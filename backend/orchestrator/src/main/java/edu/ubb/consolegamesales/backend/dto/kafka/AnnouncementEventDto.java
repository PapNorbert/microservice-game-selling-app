package edu.ubb.consolegamesales.backend.dto.kafka;

import lombok.*;

@Data()
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementEventDto {
    private Long announcementId;
    private String event;
    private Long changedByUserId;
}
