package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.controller.mapper.MessageMapper;
import edu.ubb.consolegamesales.backend.dto.kafka.MessageHistoryResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaConsumerService {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageMapper messageMapper;


    @KafkaListener(topics = "${kafkaMessageHistoryConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=edu.ubb.consolegamesales.backend.dto.kafka.MessageHistoryResponseDto"})
    public void listenToUserChattedWithTopic(MessageHistoryResponseDto messageHistoryResponseDto) {
        // got history of messages between 2 users
        // send it on websocket to listeners
        // queue format:  /queue/history/{firstId}-{otherId}
        String destination = "/queue/history/" + messageHistoryResponseDto.getFirstUserId()
                + "-" + messageHistoryResponseDto.getOtherUserId();
        messagingTemplate.convertAndSend(destination,
                messageMapper.modelsToMessageOutgoingDtos(messageHistoryResponseDto.getMessageList())
        );
    }
}
