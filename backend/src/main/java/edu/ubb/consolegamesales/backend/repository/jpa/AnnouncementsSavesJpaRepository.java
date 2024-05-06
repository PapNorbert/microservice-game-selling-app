package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.AnnouncementsSaves;
import edu.ubb.consolegamesales.backend.repository.AnnouncementsSavesRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
@Profile("jpa")
public interface AnnouncementsSavesJpaRepository extends
        AnnouncementsSavesRepository, JpaRepository<AnnouncementsSaves, Long> {

    boolean existsByAnnouncementEntityIdAndUserEntityId(Long announcementEntityId, Long userEntityId);

    @Transactional
    void deleteByAnnouncementEntityIdAndUserEntityId(Long announcementEntityId, Long userEntityId);
}
