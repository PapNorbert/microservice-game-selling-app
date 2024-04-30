package edu.ubb.consolegamesales.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Data()
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "game_discs")
public class GameDisc extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameDiscType type;
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;
    @Column(name = "game_year", nullable = false)
    private Integer gameYear;
}

