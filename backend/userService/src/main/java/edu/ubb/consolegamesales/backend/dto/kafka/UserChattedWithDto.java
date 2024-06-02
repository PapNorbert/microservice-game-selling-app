package edu.ubb.consolegamesales.backend.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChattedWithDto {
    private List<Long> userIdsList;
    private int page;
    private int limit;
    private int totalPages;
    private int totalElements;
    private Long requestUserId;
}