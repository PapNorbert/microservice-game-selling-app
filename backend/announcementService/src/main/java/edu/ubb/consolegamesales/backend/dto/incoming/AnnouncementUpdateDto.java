package edu.ubb.consolegamesales.backend.dto.incoming;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class AnnouncementUpdateDto {
    @NotNull
    private String description;
    @NotNull
    private String title;
    @NotNull
    @PositiveOrZero
    private Double price;
    @NotNull
    private Boolean transportPaidBySeller;
    @NotNull
    private Boolean sold;
    @NotNull
    private Boolean newDisc;
}
