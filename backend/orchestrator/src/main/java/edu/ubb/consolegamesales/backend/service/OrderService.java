package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.dto.kafka.OrderListOfUserReqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {
    private final String kafkaOrderListOfUserReqTopic;
    private final KafkaTemplate<String, OrderListOfUserReqDto> kafkaTemplateOrderListReq;


    public OrderService(
            @Value("${kafkaOrderListOfUserReqTopic}") String kafkaOrderListOfUserReqTopic,
            KafkaTemplate<String, OrderListOfUserReqDto> kafkaTemplateOrderListReq
    ) {
        this.kafkaOrderListOfUserReqTopic = kafkaOrderListOfUserReqTopic;
        this.kafkaTemplateOrderListReq = kafkaTemplateOrderListReq;
    }

    public void requestOrdersOfBuyerPaginated(Long buyerId,
                                              int page, int limit) {
        LOGGER.info("Sending request of orders of user {} page {} limit {}", buyerId, page, limit);
        OrderListOfUserReqDto orderListOfUserReqDto = new OrderListOfUserReqDto(buyerId, page, limit);
        kafkaTemplateOrderListReq.send(kafkaOrderListOfUserReqTopic, buyerId.toString(), orderListOfUserReqDto);
    }
}
