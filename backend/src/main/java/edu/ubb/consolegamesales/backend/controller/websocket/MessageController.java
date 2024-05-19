package edu.ubb.consolegamesales.backend.controller.websocket;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.MessageIncomingDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.MessageOutgoingDto;
import edu.ubb.consolegamesales.backend.controller.mapper.MessageMapper;
import edu.ubb.consolegamesales.backend.model.Message;
import edu.ubb.consolegamesales.backend.repository.MessageRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class MessageController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Valid @Payload MessageIncomingDto chatMessage) {
        LOGGER.info("Message sent by user {}", chatMessage.getSenderUsername());
        Message message = messageMapper.incomingDtoToModel(chatMessage);
        Message savedMessage = messageRepository.saveAndFlush(message);
        // direct to the corresponding user,
        // format: /queue/messages/{receiverId}-{senderId}
        String destination = "/queue/messages/" + chatMessage.getReceiverId()
                + "-" + chatMessage.getSenderId();
        // send message to receiver
        messagingTemplate.convertAndSend(destination, messageMapper.modelToMessageOutgoingDto(savedMessage));
    }

    @MessageMapping("/history/{firstUserID}-{otherUserId}")
    @SendToUser("/queue/reply")
    public List<MessageOutgoingDto> getMessageHistory(@DestinationVariable Long firstUserID,
                                                      @DestinationVariable Long otherUserId) {
        LOGGER.info("History request between users with Id {} and {}", firstUserID, otherUserId);
        return messageMapper.modelsToMessageOutgoingDtos(
                messageRepository.findMessagesBetweenUsersOrderedBySentTime(
                        firstUserID, otherUserId)
        );
    }
}
