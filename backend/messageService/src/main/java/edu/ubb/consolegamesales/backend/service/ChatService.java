package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.dto.kafka.UserChattedWithDto;
import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final MessageRepository messageRepository;
    private final KafkaTemplate<String, UserChattedWithDto> kafkaTemplate;
    private final String kafkaProduceTopicUserChattedWith;

    public ChatService(MessageRepository messageRepository,
                       KafkaTemplate<String, UserChattedWithDto> kafkaTemplate,
                       @Value("${kafkaUsersChatProduceTopic}") String kafkaProduceTopicUserChattedWith) {
        this.messageRepository = messageRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaProduceTopicUserChattedWith = kafkaProduceTopicUserChattedWith;
    }

    public void requestUserChattedWith(User user, int limit, int page) {
        // get user Ids that request user chatted with,
        // send them to user service to get their information
        PageRequest pageRequest =
                PageRequest.of(page - 1, limit);
        Page<Long> userIdsChattedWithPage =
                messageRepository.findDistinctUserIdsByUserChattedWith(user.getEntityId(), pageRequest);
        UserChattedWithDto userChattedWithDto = new UserChattedWithDto(
                userIdsChattedWithPage.getContent(), page, limit,
                userIdsChattedWithPage.getTotalPages(), userIdsChattedWithPage.getNumberOfElements(),
                user.getEntityId());
        kafkaTemplate.send(kafkaProduceTopicUserChattedWith, user.getEntityId().toString(), userChattedWithDto);
    }
}
