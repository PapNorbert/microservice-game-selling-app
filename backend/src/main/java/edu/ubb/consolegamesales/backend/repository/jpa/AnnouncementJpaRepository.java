package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.Announcement;
import edu.ubb.consolegamesales.backend.repository.AnnouncementRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("jpa")
public interface AnnouncementJpaRepository extends AnnouncementRepository, JpaRepository<Announcement, Long> {

    @Override
    List<Announcement> findAllBySoldOrderByCreationDateDesc(boolean sold, Pageable pageable);

    @Override
    Integer countAllBySold(boolean sold);

    @Modifying
    @Transactional
    @Query("UPDATE Announcement "
            + "SET soldGameDisc= :#{#entity.soldGameDisc}, price= :#{#entity.price}, "
            + "description= :#{#entity.description}, transportPaidBySeller= :#{#entity.transportPaidBySeller}, "
            + "sold= :#{#entity.sold}, newDisc= :#{#entity.newDisc} "
            + " WHERE entityId = :id ")
    @Override
    Integer update(Long id, Announcement entity);
}
