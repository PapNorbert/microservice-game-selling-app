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
@Table(name = "announcement_events")
public class AnnouncementEvent extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;
    @Column(nullable = false)
    private String event;
    @Column(nullable = false)
    private Date date;
    @ManyToOne
    @JoinColumn(name = "changed_by_user_id")
    private User changedByUser;
    // TODO change user
}
