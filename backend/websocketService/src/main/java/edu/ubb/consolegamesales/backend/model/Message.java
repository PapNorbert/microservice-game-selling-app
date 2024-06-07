package edu.ubb.consolegamesales.backend.model;

import lombok.*;

import java.util.Date;

@Data()
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Message extends BaseEntity {
    private Long senderId;
    private String senderUsername;
    private Long receiverId;
    private String data;
    private Date sentTime;
}
