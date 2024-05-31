package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.Announcement;
import edu.ubb.consolegamesales.backend.repository.AnnouncementRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@Profile("jpa")
public interface AnnouncementJpaRepository extends
        AnnouncementRepository, JpaRepository<Announcement, Long>,
        JpaSpecificationExecutor<Announcement> {

    @Modifying
    @Transactional
    @Query("UPDATE Announcement "
            + "SET price= :#{#entity.price}, title= :#{#entity.title}, "
            + "description= :#{#entity.description}, transportPaidBySeller= :#{#entity.transportPaidBySeller}, "
            + "sold= :#{#entity.sold}, newDisc= :#{#entity.newDisc} "
            + " WHERE entityId = :id ")
    @Override
    Integer update(Long id, Announcement entity);

    Optional<Announcement> findByEntityId(Long id);
}
