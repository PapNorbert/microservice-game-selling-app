package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.AnnouncementsSaves;
import edu.ubb.consolegamesales.backend.repository.AnnouncementsSavesRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
@Profile("jpa")
public interface AnnouncementsSavesJpaRepository extends
        AnnouncementsSavesRepository, JpaRepository<AnnouncementsSaves, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE AnnouncementsSaves "
            + "SET announcement= :#{#entity} "
            + " WHERE entityId = :id ")
    @Override
    Integer update(Long id, AnnouncementsSaves entity);

    boolean existsByAnnouncementEntityIdAndUserId(Long announcementEntityId, Long userEntityId);

    @Transactional
    void deleteByAnnouncementEntityIdAndUserId(Long announcementEntityId, Long userEntityId);

    @Transactional
    void deleteByAnnouncementEntityId(Long announcementEntityId);
}
