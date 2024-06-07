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
@Table(name = "messages")
public class Message extends BaseEntity {
    @Column(name = "sender_id")
    private Long senderId;
    @Column(name = "sender_username")
    private String senderUsername;
    @Column(name = "receiver_id")
    private Long receiverId;
    @Column(nullable = false, length = 855)
    private String data;
    @Column(nullable = false, name = "sent_time")
    private Date sentTime;
}
