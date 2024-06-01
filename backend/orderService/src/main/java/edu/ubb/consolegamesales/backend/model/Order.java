package edu.ubb.consolegamesales.backend.model;

import jakarta.persistence.*;
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
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;
    // TODO change User
    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;
    // TODO change Announcement
    @Column(nullable = false, name = "order_date")
    private Date orderDate;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private String orderAddress;
}
