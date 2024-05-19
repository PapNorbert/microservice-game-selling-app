package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.Message;
import edu.ubb.consolegamesales.backend.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
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
            + "(m.sender.entityId = :userId1 AND m.receiver.entityId = :userId2) OR "
            + "(m.sender.entityId = :userId2 AND m.receiver.entityId = :userId1) "
            + "ORDER BY m.sentTime")
    List<Message> findMessagesBetweenUsersOrderedBySentTime(Long userId1, Long userId2);
}
