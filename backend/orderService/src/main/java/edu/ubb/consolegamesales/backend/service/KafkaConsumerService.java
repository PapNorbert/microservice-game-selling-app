package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.dto.kafka.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaConsumerService {
    private final OrderService orderService;
    private final OrderTransactionService orderTransactionService;

    @KafkaListener(topics = "${kafkaOrderListOfUserReqTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.OrderListOfUserReqDto"})
    public void listenToOrderListOfUserReqTopic(OrderListOfUserReqDto orderListOfUserReqDto) {
        LOGGER.info("Got request of orders of user {}", orderListOfUserReqDto.getUserId());
        orderService.findAllOrdersOfUser(orderListOfUserReqDto.getUserId(),
                orderListOfUserReqDto.getPage(), orderListOfUserReqDto.getLimit());
    }

    @KafkaListener(topics = "${kafkaOrderDataReqConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.OrderDataReqDto"})
    public void listenToOrderDataReqTopic(OrderDataReqDto orderDataReqDto) {
        LOGGER.info("Got request of order {}", orderDataReqDto.getOrderId());
        orderService.findOrderById(orderDataReqDto.getOrderId());
    }

    @KafkaListener(topics = "${kafkaOrderTransactionOrderCreateConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.TransactionOrderCreationDto"})
    public void listenToOrderCreateTopic(TransactionOrderCreationDto transactionOrderCreationDto) {
        LOGGER.info("Order creation request for announcement {}",
                transactionOrderCreationDto.getOrderCreationDto().getAnnouncementId());
        orderTransactionService.createOrder(transactionOrderCreationDto);
    }

    @KafkaListener(topics = "${kafkaOrderTransactionOrderDeleteConsumeTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.TransactionOrderDeletionDto"})
    public void listenToOrderDeleteTopic(TransactionOrderDeletionDto transactionOrderDeletionDto) {
        LOGGER.info("Delete request of order {}",
                transactionOrderDeletionDto.getOrderId());
        orderTransactionService.deleteOrder(transactionOrderDeletionDto);
    }

    @KafkaListener(topics = "${kafkaOrderTransactionCompensationProduceTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type="
                    + "edu.ubb.consolegamesales.backend.dto.kafka.TransactionCompensationDto"})
    public void listenToOrderDeleteTopic(
            TransactionCompensationDto transactionCompensationDto) {
        LOGGER.info("Transaction failed, received compensation request, order {}",
                transactionCompensationDto.getOrder().getEntityId());
        orderTransactionService.compensateTransaction(transactionCompensationDto);
    }
}
