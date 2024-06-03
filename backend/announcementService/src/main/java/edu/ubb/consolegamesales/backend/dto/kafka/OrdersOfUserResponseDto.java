package edu.ubb.consolegamesales.backend.dto.kafka;

import edu.ubb.consolegamesales.backend.dto.outgoing.OrderListWithPaginationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersOfUserResponseDto {
    private Long userId;
    private OrderListWithPaginationDto orders;
}
