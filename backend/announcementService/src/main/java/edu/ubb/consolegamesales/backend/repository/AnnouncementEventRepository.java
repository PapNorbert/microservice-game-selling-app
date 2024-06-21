package edu.ubb.consolegamesales.backend.repository;


import edu.ubb.consolegamesales.backend.model.AnnouncementEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnnouncementEventRepository {
    AnnouncementEvent saveAndFlush(AnnouncementEvent announcementEvent);

    Page<AnnouncementEvent> findAll(Pageable pageable);

}
