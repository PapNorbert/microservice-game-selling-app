package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.Message;
import edu.ubb.consolegamesales.backend.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("jpa")
public interface MessageJpaRepository extends MessageRepository, JpaRepository<Message, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Message "
            + "SET data= :#{#entity.data}, sentTime= :#{#entity.sentTime} "
            + " WHERE entityId = :id ")
    @Override
    Integer update(Long id, Message entity);

    @Query("FROM Message m WHERE "
            + "(m.senderId = :userId1 AND m.receiverId = :userId2) OR "
            + "(m.senderId = :userId2 AND m.receiverId = :userId1) "
            + "ORDER BY m.sentTime")
    List<Message> findMessagesBetweenUsersOrderedBySentTime(Long userId1, Long userId2);

    @Query("SELECT DISTINCT CASE "
            + "WHEN m.senderId = :userId THEN m.receiverId "
            + "WHEN m.receiverId = :userId THEN m.senderId "
            + "END "
            + "FROM Message m "
            + "WHERE m.senderId = :userId OR m.receiverId = :userId")
    Page<Long> findDistinctUserIdsByUserChattedWith(Long userId, Pageable pageable);

}
