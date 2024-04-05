package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.Message;
import edu.ubb.consolegamesales.backend.repository.MessageRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public interface MessageJpaRepository extends MessageRepository, JpaRepository<Message, Long> {
}
