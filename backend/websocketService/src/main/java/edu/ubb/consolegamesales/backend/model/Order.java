package edu.ubb.consolegamesales.backend.model;

import lombok.*;

import java.util.Date;

@Data()
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {
    private Long buyerId;
    private Long announcementId;
    private Date orderDate;
    private Double price;
    private String orderAddress;
}
