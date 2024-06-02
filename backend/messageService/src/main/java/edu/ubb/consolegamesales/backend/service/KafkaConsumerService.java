package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.dto.kafka.MessageHistoryRequestDto;
import edu.ubb.consolegamesales.backend.dto.kafka.MessageHistoryResponseDto;
import edu.ubb.consolegamesales.backend.dto.kafka.MessageToSaveDto;
import edu.ubb.consolegamesales.backend.model.Message;
import edu.ubb.consolegamesales.backend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class KafkaConsumerService {
    private final MessageRepository messageRepository;
    private final String kafkaMessageHistoryProduceTopic;
    private final KafkaTemplate<String, MessageHistoryResponseDto> kafkaTemplateMessageHistory;

    public KafkaConsumerService(MessageRepository messageRepository,
                                @Value("${kafkaMessageHistoryProduceTopic}") String kafkaMessageHistoryProduceTopic,
                                KafkaTemplate<String, MessageHistoryResponseDto> kafkaTemplateMessageHistory
    ) {
        this.messageRepository = messageRepository;
        this.kafkaMessageHistoryProduceTopic = kafkaMessageHistoryProduceTopic;
        this.kafkaTemplateMessageHistory = kafkaTemplateMessageHistory;
    }

    @KafkaListener(topics = "${kafkaMessageSaveConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=edu.ubb.consolegamesales.backend.dto.kafka.MessageToSaveDto"})
    public void listenToUserChattedWithTopic(MessageToSaveDto messageToSaveDto) {
        messageRepository.saveAndFlush(messageToSaveDto.getMessage());
    }

    @KafkaListener(topics = "${kafkaMessageHistoryConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=edu.ubb.consolegamesales.backend.dto.kafka.MessageHistoryRequestDto"})
    public void listenToMessageHistoryRequestTopic(MessageHistoryRequestDto messageHistoryRequestDto) {
        List<Message> messageList =
                messageRepository.findMessagesBetweenUsersOrderedBySentTime(
                        messageHistoryRequestDto.getFirstUserId(), messageHistoryRequestDto.getOtherUserId());
        MessageHistoryResponseDto messageHistoryResponseDto =
                new MessageHistoryResponseDto(  messageHistoryRequestDto.getFirstUserId(),
                        messageHistoryRequestDto.getOtherUserId(), messageList);
        kafkaTemplateMessageHistory.send(kafkaMessageHistoryProduceTopic,
                UUID.randomUUID().toString(), messageHistoryResponseDto);
    }
}
