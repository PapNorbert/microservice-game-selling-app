package edu.ubb.consolegamesales.backend.repository;

import edu.ubb.consolegamesales.backend.model.Message;

import java.util.List;

public interface MessageRepository extends Repository<Message, Long> {
    List<Message> findMessagesBetweenUsersOrderedBySentTime(Long userId1, Long userId2);

}
