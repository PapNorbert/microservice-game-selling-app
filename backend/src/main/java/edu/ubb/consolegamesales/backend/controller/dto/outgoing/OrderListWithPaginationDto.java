package edu.ubb.consolegamesales.backend.controller.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderListWithPaginationDto {
    List<OrderListDto> orders;
    Pagination pagination;
}


