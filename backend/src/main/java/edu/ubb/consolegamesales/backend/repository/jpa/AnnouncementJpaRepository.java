package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.Announcement;
import edu.ubb.consolegamesales.backend.repository.AnnouncementRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public interface AnnouncementJpaRepository extends AnnouncementRepository, JpaRepository<Announcement, Long> {
}
