package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.dto.kafka.UserChattedWithDto;
import edu.ubb.consolegamesales.backend.dto.kafka.UsersChattedWithResponseDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.UsersResponseWithPaginationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {
    private final UserService userService;
    private final KafkaTemplate<String, UsersChattedWithResponseDto> kafkaTemplateUsersChattedWithResponse;
    private final String kafkaUsersChattedWithResponseProduceTopic;

    public KafkaConsumerService(
            UserService userService,
            @Value("${kafkaUsersChattedWithResponseProduceTopic}")
            String kafkaUsersChattedWithResponseProduceTopic,
            KafkaTemplate<String, UsersChattedWithResponseDto> kafkaTemplateUsersChattedWithResponse
    ) {
        this.userService = userService;
        this.kafkaUsersChattedWithResponseProduceTopic = kafkaUsersChattedWithResponseProduceTopic;
        this.kafkaTemplateUsersChattedWithResponse = kafkaTemplateUsersChattedWithResponse;
    }

    @KafkaListener(topics = "${kafkaUsersChatConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.UserChattedWithDto"})
    public void listenToUserChattedWithTopic(UserChattedWithDto userChattedWithDto) {
        LOGGER.info("Received request for {} users data, "
                        + "that chatted with user with id : {}",
                userChattedWithDto.getUserIdsList(), userChattedWithDto.getRequestUserId());
        UsersResponseWithPaginationDto usersResponseWithPaginationDto =
                userService.loadUsersByUserChattedWith(userChattedWithDto);
        // send the response to the websocket server for delivery to client
        UsersChattedWithResponseDto usersChattedWithResponseDto =
                new UsersChattedWithResponseDto(usersResponseWithPaginationDto, userChattedWithDto.getRequestUserId());
        kafkaTemplateUsersChattedWithResponse.send(
                kafkaUsersChattedWithResponseProduceTopic, usersChattedWithResponseDto
        );
    }
}
