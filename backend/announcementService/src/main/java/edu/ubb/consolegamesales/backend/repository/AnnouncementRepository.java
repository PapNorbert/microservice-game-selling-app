package edu.ubb.consolegamesales.backend.repository;

import edu.ubb.consolegamesales.backend.model.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;


public interface AnnouncementRepository extends Repository<Announcement, Long> {
    Page<Announcement> findAll(Specification<Announcement> spec, Pageable pageable);

    Optional<Announcement> findByEntityId(Long id);

}
