package edu.ubb.consolegamesales.backend.repository;

import edu.ubb.consolegamesales.backend.model.Announcement;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnnouncementRepository extends Repository<Announcement, Long> {
    List<Announcement> findAllBySoldOrderByCreationDateDesc(boolean sold, Pageable pageable);

    Integer countAllBySold(boolean sold);
}
