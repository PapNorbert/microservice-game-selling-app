package edu.ubb.consolegamesales.backend.controller.websocket;

import edu.ubb.consolegamesales.backend.controller.mapper.MessageMapper;
import edu.ubb.consolegamesales.backend.dto.incoming.MessageIncomingDto;
import edu.ubb.consolegamesales.backend.dto.kafka.MessageHistoryRequestDto;
import edu.ubb.consolegamesales.backend.dto.kafka.MessageToSaveDto;
import edu.ubb.consolegamesales.backend.model.Message;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@Slf4j
public class MessageController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageMapper messageMapper;
    private final KafkaTemplate<String, MessageToSaveDto> kafkaTemplateMessageSave;
    private final String kafkaMessageSaveProduceTopic;
    private final KafkaTemplate<String, MessageHistoryRequestDto> kafkaTemplateMessageHistoryRequest;
    private final String kafkaMessageHistoryProduceTopic;

    public MessageController(SimpMessagingTemplate messagingTemplate,
                             MessageMapper messageMapper,
                             KafkaTemplate<String, MessageToSaveDto> kafkaTemplateMessageSave,
                             KafkaTemplate<String, MessageHistoryRequestDto> kafkaTemplateMessageHistoryRequest,
                             @Value("${kafkaMessageSaveProduceTopic}") String kafkaMessageSaveProduceTopic,
                             @Value("${kafkaMessageHistoryProduceTopic}") String kafkaMessageHistoryProduceTopic
    ) {
        this.messagingTemplate = messagingTemplate;
        this.messageMapper = messageMapper;
        this.kafkaTemplateMessageSave = kafkaTemplateMessageSave;
        this.kafkaTemplateMessageHistoryRequest = kafkaTemplateMessageHistoryRequest;
        this.kafkaMessageSaveProduceTopic = kafkaMessageSaveProduceTopic;
        this.kafkaMessageHistoryProduceTopic = kafkaMessageHistoryProduceTopic;
    }


    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Valid @Payload MessageIncomingDto chatMessage) {
        LOGGER.info("Message sent by user {}", chatMessage.getSenderUsername());
        Message message = messageMapper.incomingDtoToModel(chatMessage);
        // send message to save user
        MessageToSaveDto messageToSaveDto = new MessageToSaveDto(message);
        kafkaTemplateMessageSave.send(kafkaMessageSaveProduceTopic,
                UUID.randomUUID().toString(), messageToSaveDto);
        // direct to the corresponding user,
        // format: /queue/messages/{receiverId}-{senderId}
        String destination = "/queue/messages/" + chatMessage.getReceiverId()
                + "-" + chatMessage.getSenderId();
        // send message to receiver
        messagingTemplate.convertAndSend(destination, messageMapper.modelToMessageOutgoingDto(message));
    }

    @MessageMapping("/history/{firstUserID}-{otherUserId}")
    public void getMessageHistory(@DestinationVariable Long firstUserID,
                                  @DestinationVariable Long otherUserId) {
        LOGGER.info("History request between users with Id {} and {}", firstUserID, otherUserId);
        MessageHistoryRequestDto messageHistoryRequestDto = new MessageHistoryRequestDto(
                firstUserID, otherUserId
        );
        // send a request to get the message history from message service
        // listens for response in KafkaConsumerService
        kafkaTemplateMessageHistoryRequest.send(kafkaMessageHistoryProduceTopic,
                UUID.randomUUID().toString(), messageHistoryRequestDto);
    }
}
