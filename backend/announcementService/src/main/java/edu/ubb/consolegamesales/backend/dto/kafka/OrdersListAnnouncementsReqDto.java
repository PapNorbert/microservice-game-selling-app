package edu.ubb.consolegamesales.backend.dto.kafka;

import edu.ubb.consolegamesales.backend.dto.outgoing.Pagination;
import edu.ubb.consolegamesales.backend.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersListAnnouncementsReqDto {
    private Long userId;
    private Pagination pagination;
    private List<Order> orders;
}
