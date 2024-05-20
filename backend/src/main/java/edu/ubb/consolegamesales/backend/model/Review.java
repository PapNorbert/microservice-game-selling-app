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
@Table(name = "reviews")
public class Review extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;
    @Column(nullable = false)
    private Date creationDate;
    @Column(nullable = false, name = "review_text", length = 855)
    private String reviewText;
}
