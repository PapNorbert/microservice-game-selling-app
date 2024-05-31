package edu.ubb.consolegamesales.backend.dto.incoming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ReviewCreationDto {
    @NotNull
    @Positive
    private Long reviewerId;
    @NotNull
    @Positive
    private Long sellerId;
    @NotBlank
    private String reviewText;
}
