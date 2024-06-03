package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.controller.mapper.MessageMapper;
import edu.ubb.consolegamesales.backend.dto.kafka.MessageHistoryResponseDto;
import edu.ubb.consolegamesales.backend.dto.kafka.OrdersOfUserResponseDto;
import edu.ubb.consolegamesales.backend.dto.kafka.UsersChattedWithResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaConsumerService {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageMapper messageMapper;


    @KafkaListener(topics = "${kafkaMessageHistoryConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.MessageHistoryResponseDto"})
    public void listenToMessageHistoryTopic(MessageHistoryResponseDto messageHistoryResponseDto) {
        // got history of messages between 2 users
        // send it on websocket to listeners
        // queue format:  /queue/history/{firstId}-{otherId}
        String destination = "/queue/history/" + messageHistoryResponseDto.getFirstUserId()
                + "-" + messageHistoryResponseDto.getOtherUserId();
        LOGGER.info("Sending response with message history to {}", destination);
        messagingTemplate.convertAndSend(destination,
                messageMapper.modelsToMessageOutgoingDtos(messageHistoryResponseDto.getMessageList())
        );
    }

    @KafkaListener(topics = "${kafkaUsersChattedWithResponseConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.UsersChattedWithResponseDto"})
    public void listenToUserChattedWithResponseTopic(UsersChattedWithResponseDto usersChattedWithResponseDto) {
        // sending on websocket to listener users that chatted with requestUserId
        // queue format:  /queue/chats/{requestUserId}
        String destination = "/queue/chats/" + usersChattedWithResponseDto.getRequestUserId().toString();
        LOGGER.info("Sending response with users chatted with to {}", destination);
        messagingTemplate.convertAndSend(destination,
                usersChattedWithResponseDto.getUsersResponse()
        );
    }

    @KafkaListener(topics = "${kafkaOrdersListAnnouncementsResponseConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.OrdersOfUserResponseDto"})
    public void listenToUserChattedWithResponseTopic(OrdersOfUserResponseDto ordersOfUserResponseDto) {
        // sending on websocket to listener orders of user
        // queue format:  /queue/orders/{requestUserId}
        String destination = "/queue/orders/" + ordersOfUserResponseDto.getUserId().toString();
        LOGGER.info("Sending response with user orders to {}", destination);
        messagingTemplate.convertAndSend(destination,
                ordersOfUserResponseDto.getOrders()
        );
    }

}
