package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.dto.kafka.OrderListOfUserReqDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaConsumerService {
    private final OrderService orderService;

    @KafkaListener(topics = "${kafkaOrderListOfUserReqTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.OrderListOfUserReqDto"})
    public void listenToMessageHistoryTopic(OrderListOfUserReqDto orderListOfUserReqDto) {
        LOGGER.info("Got request of orders of user {}", orderListOfUserReqDto.getUserId());
        orderService.findAllOrdersOfUser(orderListOfUserReqDto.getUserId(),
                orderListOfUserReqDto.getPage(), orderListOfUserReqDto.getLimit());
    }
}
