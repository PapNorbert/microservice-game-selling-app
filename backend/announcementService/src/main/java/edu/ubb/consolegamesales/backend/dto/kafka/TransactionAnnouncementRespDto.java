package edu.ubb.consolegamesales.backend.dto.kafka;

import edu.ubb.consolegamesales.backend.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionAnnouncementRespDto {
    private Order order;
    private Long announcementId;
    private boolean transactionSuccess;
}
