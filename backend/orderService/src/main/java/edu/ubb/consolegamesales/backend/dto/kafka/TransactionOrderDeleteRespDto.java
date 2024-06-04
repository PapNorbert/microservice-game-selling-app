package edu.ubb.consolegamesales.backend.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionOrderDeleteRespDto {
    private Long orderId;
    private Long announcementId;
    private boolean transactionSuccess;
}
