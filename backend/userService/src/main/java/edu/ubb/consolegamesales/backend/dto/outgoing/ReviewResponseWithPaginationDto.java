package edu.ubb.consolegamesales.backend.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReviewResponseWithPaginationDto {
    List<ReviewResponseDto> reviews;
    Pagination pagination;
}
