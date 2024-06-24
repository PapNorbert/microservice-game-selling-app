package edu.ubb.consolegamesales.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data()
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "announcements")
public class Announcement extends BaseEntity {
    @Column(name = "seller_id")
    private Long sellerId;
    @ManyToOne
    @JoinColumn(name = "game_disc_id")
    private GameDisc soldGameDisc;
    @Column(nullable = false, length = 855)
    private String description;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false, name = "transport_paid_by_seller")
    private Boolean transportPaidBySeller;
    @Column(nullable = false, name = "new_disc")
    private Boolean newDisc;
    @Column(nullable = false)
    private Boolean sold;
    @Column(nullable = false)
    private Date creationDate;

    @JsonIgnore
    @OneToMany(mappedBy = "announcement", cascade = CascadeType.REMOVE,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AnnouncementEvent> announcementEvents;
    @JsonIgnore
    @OneToMany(mappedBy = "announcement", cascade = CascadeType.REMOVE,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AnnouncementsSaves> announcementsSaves;
}
