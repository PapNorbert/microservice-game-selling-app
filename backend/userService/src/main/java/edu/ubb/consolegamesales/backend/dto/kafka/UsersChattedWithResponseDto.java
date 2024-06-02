package edu.ubb.consolegamesales.backend.dto.kafka;

import edu.ubb.consolegamesales.backend.dto.outgoing.UsersResponseWithPaginationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersChattedWithResponseDto {
    private UsersResponseWithPaginationDto usersResponse;
    private Long requestUserId;
}
