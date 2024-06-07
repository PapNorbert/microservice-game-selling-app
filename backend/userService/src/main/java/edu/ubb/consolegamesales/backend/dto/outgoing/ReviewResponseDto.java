package edu.ubb.consolegamesales.backend.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private String reviewText;
    private Date creationDate;
    private UserResponseDto reviewer;
}
