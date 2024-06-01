package edu.ubb.consolegamesales.backend.dto.outgoing;

import lombok.Data;

import java.util.Date;

@Data
public class OrderListDto {
    private Long orderId;
    private Date orderDate;
    private Double price;
    private String orderAddress;
    private AnnouncementListShortDto announcement;
    // TODO change AnnouncementListShortDto
}
