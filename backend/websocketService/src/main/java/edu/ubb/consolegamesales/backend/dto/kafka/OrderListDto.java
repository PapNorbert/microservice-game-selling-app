package edu.ubb.consolegamesales.backend.dto.kafka;

import edu.ubb.consolegamesales.backend.dto.outgoing.AnnouncementListShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListDto {
    private Long orderId;
    private Date orderDate;
    private Double price;
    private String orderAddress;
    private AnnouncementListShortDto announcement;
}
