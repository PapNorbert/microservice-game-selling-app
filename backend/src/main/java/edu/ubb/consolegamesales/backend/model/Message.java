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
@Table(name = "messages")
public class Message extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;
    @Column(nullable = false, length = 855)
    private String data;
    @Column(nullable = false, name = "sent_time")
    private Date sentTime;
}
