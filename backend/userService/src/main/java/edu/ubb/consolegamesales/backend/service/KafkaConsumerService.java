package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.dto.kafka.UserChattedWithDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {
    private final UserService userService;
    private final String kafkaConsumeTopicUserChattedWith;

    public KafkaConsumerService(UserService userService,
                                @Value("${kafkaUsersChatConsumeTopic}") String kafkaConsumeTopicUserChattedWith) {
        this.userService = userService;
        this.kafkaConsumeTopicUserChattedWith = kafkaConsumeTopicUserChattedWith;
    }

    @KafkaListener(topics = "${kafkaUsersChatConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=edu.ubb.consolegamesales.backend.dto.kafka.UserChattedWithDto"})
    public void listenToUserChattedWithTopic(UserChattedWithDto userChattedWithDto) {
        LOGGER.info("Received request for {} users data, "
                        + "that chatted with user with id : {}",
                userChattedWithDto.getUserIdsPage(), userChattedWithDto.getRequestUserId());

//                List<UserResponseDto> userResponseDtos = userMapper.modelsToResponseDtos(usersChattedWith.getContent());
//        Pagination pagination = new Pagination(page, limit,
//                usersChattedWith.getTotalElements(), usersChattedWith.getTotalPages());
//        return new UsersResponseWithPaginationDto(userResponseDtos, pagination);
// TODO listenToUserChattedWithTopic

    }
}
