package edu.ubb.consolegamesales.backend.dto.incoming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreationDto {
    @NotNull
    @Positive
    private Long buyerId;
    @NotNull
    @Positive
    private Long announcementId;
    @NotNull
    @Positive
    private Double price;
    @NotBlank
    private String orderAddress;
}
