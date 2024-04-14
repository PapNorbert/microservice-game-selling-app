package edu.ubb.consolegamesales.backend.controller.dto.incoming;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class AnnouncementCreationDto {
    @NotNull @Positive
    private Long sellerId;
    @NotNull @Positive
    private Long soldGameDiscId;
    @NotNull
    private String description;
    @NotNull @PositiveOrZero
    private Double price;
}
