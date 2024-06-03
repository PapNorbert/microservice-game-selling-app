package edu.ubb.consolegamesales.backend.dto.kafka;

import edu.ubb.consolegamesales.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionOrderDeletionDto {
    private Long orderId;
    private User user;
}
