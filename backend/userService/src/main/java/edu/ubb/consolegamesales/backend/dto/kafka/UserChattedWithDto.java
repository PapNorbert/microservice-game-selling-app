package edu.ubb.consolegamesales.backend.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChattedWithDto {
    Page<Long> userIdsPage;
    Long requestUserId;
}
