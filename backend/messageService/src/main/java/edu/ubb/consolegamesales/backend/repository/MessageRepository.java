package edu.ubb.consolegamesales.backend.repository;

import edu.ubb.consolegamesales.backend.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageRepository extends Repository<Message, Long> {
    List<Message> findMessagesBetweenUsersOrderedBySentTime(Long userId1, Long userId2);

    Page<Long> findDistinctUserIdsByUserChattedWith(Long userId, Pageable pageable);
}
