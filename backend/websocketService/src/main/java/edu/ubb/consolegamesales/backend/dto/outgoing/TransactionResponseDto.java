package edu.ubb.consolegamesales.backend.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionResponseDto {
    private Long orderId;
    private boolean transactionSuccess;
}
