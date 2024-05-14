package edu.ubb.consolegamesales.backend.controller.websocket;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.MessageIncomingDto;
import edu.ubb.consolegamesales.backend.controller.mapper.MessageMapper;
import edu.ubb.consolegamesales.backend.model.Message;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageMapper messageMapper;

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(MessageIncomingDto chatMessage) {
        Message message = messageMapper.incomingDtoToModel(chatMessage);
        // direct to the corresponding user
        String destination = "/queue/messages-" + message.getReceiver();
        messagingTemplate.convertAndSend(destination, message);
    }


}
