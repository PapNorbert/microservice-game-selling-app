package edu.ubb.consolegamesales.backend.repository;

import edu.ubb.consolegamesales.backend.model.Announcement;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AnnouncementRepository extends Repository<Announcement, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Announcement "
            + "SET soldGameDisc= :#{#entity.soldGameDisc}, price= :#{#entity.price}, "
            + "description= :#{#entity.description} WHERE entityId = :id ")
    @Override
    Long update(Long id, Announcement entity);
}
