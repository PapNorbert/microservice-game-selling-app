package edu.ubb.consolegamesales.backend.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class AnnouncementsListWithPaginationDto {
    Collection<AnnouncementListShortDto> announcements;
    Pagination pagination;
}
