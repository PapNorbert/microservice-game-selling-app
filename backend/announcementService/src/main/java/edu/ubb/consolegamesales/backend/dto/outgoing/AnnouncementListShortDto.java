package edu.ubb.consolegamesales.backend.dto.outgoing;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.Date;

@Data
public class AnnouncementListShortDto {
    @NotNull
    @Positive
    private Long announcementId;
    @NotNull
    private String soldGameDiscName;
    @NotNull
    private String soldGameDiscType;
    @NotNull
    @Positive
    private Long sellerId;
    @NotNull
    private String title;
    @NotNull
    @PositiveOrZero
    private Double price;
    @NotNull
    private Boolean transportPaidBySeller;
    @NotNull
    private Boolean newDisc;
    @NotNull
    private Date creationDate;
    @NotNull
    private boolean savedByUser;
}
