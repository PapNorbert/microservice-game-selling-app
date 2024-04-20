package edu.ubb.consolegamesales.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Data()
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "announcements")
public class Announcement extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;
    @ManyToOne
    @JoinColumn(name = "game_disc_id")
    private GameDisc soldGameDisc;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false, name = "transport_paid_by_seller")
    private Boolean transportPaidBySeller;
}
