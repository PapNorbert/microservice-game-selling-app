package edu.ubb.consolegamesales.backend.model;

import lombok.*;

import java.util.Date;

@Data()
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Announcement extends BaseEntity {
    private Long sellerId;
    private GameDisc soldGameDisc;
    private String description;
    private String title;
    private Double price;
    private Boolean transportPaidBySeller;
    private Boolean newDisc;
    private Boolean sold;
    private Date creationDate;
}
