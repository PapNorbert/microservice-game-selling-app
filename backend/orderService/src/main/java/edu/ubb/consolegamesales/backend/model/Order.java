package edu.ubb.consolegamesales.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Data()
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Column(name = "buyer_id")
    private Long buyerId;
    @Column(name = "announcement_id")
    private Long announcementId;
    @Column(nullable = false, name = "order_date")
    private Date orderDate;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private String orderAddress;
}
