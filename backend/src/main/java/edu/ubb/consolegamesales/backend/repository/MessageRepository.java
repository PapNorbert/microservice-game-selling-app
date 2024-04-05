package edu.ubb.consolegamesales.backend.repository;

import edu.ubb.consolegamesales.backend.model.Message;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends Repository<Message, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Message "
            + "SET text= :#{#entity.text}, sentTime= :#{#entity.sentTime} "
            + " WHERE entityId = :id ")
    @Override
    Long update(Long id, Message entity);
}
