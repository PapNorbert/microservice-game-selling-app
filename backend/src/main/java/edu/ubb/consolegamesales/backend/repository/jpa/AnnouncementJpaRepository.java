package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.Announcement;
import edu.ubb.consolegamesales.backend.repository.AnnouncementRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public interface AnnouncementJpaRepository extends AnnouncementRepository, JpaRepository<Announcement, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Announcement "
            + "SET soldGameDisc= :#{#entity.soldGameDisc}, price= :#{#entity.price}, "
            + "description= :#{#entity.description} WHERE entityId = :id ")
    @Override
    Integer update(Long id, Announcement entity);
}
