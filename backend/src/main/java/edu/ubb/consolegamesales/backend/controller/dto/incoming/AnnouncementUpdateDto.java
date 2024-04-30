package edu.ubb.consolegamesales.backend.controller.dto.incoming;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class AnnouncementUpdateDto {
    @NotNull @Positive
    private Long soldGameDiscId;
    @NotNull
    private String description;
    @NotNull @PositiveOrZero
    private Double price;
    @NotNull
    private Boolean transportPaidBySeller;
    @NotNull
    private Boolean sold;
    @NotNull
    private Boolean newDisc;
}
