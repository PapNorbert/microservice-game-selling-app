package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.AnnouncementEvent;
import edu.ubb.consolegamesales.backend.repository.AnnouncementEventRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementEventJpaRepository extends
        AnnouncementEventRepository, JpaRepository<AnnouncementEvent, Long> {
}
