package edu.ubb.consolegamesales.backend.dto.outgoing;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class OrderListDto {
    private Long orderId;
    private Date orderDate;
    private Double price;
    private String orderAddress;
    private AnnouncementListShortDto announcement;
}
