package edu.ubb.consolegamesales.backend.dto.outgoing;

import edu.ubb.consolegamesales.backend.model.AnnouncementEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementEventsPaginated {
    Collection<AnnouncementEvent> announcementEvents;
    Pagination pagination;
}
