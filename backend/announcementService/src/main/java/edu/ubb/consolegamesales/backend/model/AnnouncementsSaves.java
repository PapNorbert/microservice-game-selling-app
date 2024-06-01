package edu.ubb.consolegamesales.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Data()
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "announcements_saves")
public class AnnouncementsSaves extends BaseEntity {
    @Column(name = "user_id")
    private Long userId;
    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;
}
