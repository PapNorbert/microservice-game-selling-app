package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.Message;
import edu.ubb.consolegamesales.backend.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public interface MessageJpaRepository extends MessageRepository, JpaRepository<Message, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Message "
            + "SET text= :#{#entity.text}, sentTime= :#{#entity.sentTime} "
            + " WHERE entityId = :id ")
    @Override
    Integer update(Long id, Message entity);
}
