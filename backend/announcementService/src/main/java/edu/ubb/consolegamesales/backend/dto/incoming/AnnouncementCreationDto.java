package edu.ubb.consolegamesales.backend.dto.incoming;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class AnnouncementCreationDto {
    @NotNull
    @Positive
    private Long sellerId;
    @NotNull @NotBlank
    private String description;
    @NotNull @NotBlank
    private String title;
    @NotNull
    @PositiveOrZero
    private Double price;
    @NotNull
    private Boolean transportPaidBySeller;
    @NotNull
    private Boolean newDisc;
    @NotNull @Valid
    private GameDiscCreationDto soldGameDisc;
}
