package edu.ubb.consolegamesales.backend.controller.dto.outgoing;

import edu.ubb.consolegamesales.backend.model.GameDisc;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.Date;

@Data
public class AnnouncementDetailedDto {
    @NotNull @Positive
    private Long announcementId;
    @NotNull @Positive
    private Long sellerId;
    @NotNull
    private String sellerUsername;
    @NotNull @Positive
    private GameDisc soldGameDisc;
    @NotNull @NotEmpty
    private String description;
    @NotNull
    private String title;
    @NotNull @PositiveOrZero
    private Double price;
    @NotNull
    private Boolean transportPaidBySeller;
    @NotNull
    private Boolean sold;
    @NotNull
    private Boolean newDisc;
    @NotNull
    private Date creationDate;
}
