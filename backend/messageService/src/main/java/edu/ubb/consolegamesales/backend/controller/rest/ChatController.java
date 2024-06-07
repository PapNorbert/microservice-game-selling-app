package edu.ubb.consolegamesales.backend.controller.rest;

import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.service.ChatService;
import edu.ubb.consolegamesales.backend.service.security.AuthenticationInformation;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("${apiPrefix}/chats")
@AllArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void findUsersChattedWith(
            @RequestParam(defaultValue = "1", required = false) @Positive int page,
            @RequestParam(defaultValue = "5", required = false) @Positive int limit,
            Authentication authentication) {
        User user = AuthenticationInformation.extractUser(authentication);
        LOGGER.info("GET at chats/users api, get users that has messages with user with id {}",
                user.getEntityId());
        chatService.requestUserChattedWith(user, limit, page);
        // response status is ACCEPTED, data will be sent later through websocket
    }

}
