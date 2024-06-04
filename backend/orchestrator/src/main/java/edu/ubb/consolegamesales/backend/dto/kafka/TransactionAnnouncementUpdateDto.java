package edu.ubb.consolegamesales.backend.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionAnnouncementUpdateDto {
    private Long orderId;
    private Long announcementId;
}
