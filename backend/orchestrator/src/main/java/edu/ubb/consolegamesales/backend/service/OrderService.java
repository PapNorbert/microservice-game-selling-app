package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.controller.mapper.AnnouncementMapper;
import edu.ubb.consolegamesales.backend.dto.incoming.OrderCreationDto;
import edu.ubb.consolegamesales.backend.dto.kafka.*;
import edu.ubb.consolegamesales.backend.dto.outgoing.OrderListDto;
import edu.ubb.consolegamesales.backend.model.Announcement;
import edu.ubb.consolegamesales.backend.model.Order;
import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.service.exception.DataUnderProcessingException;
import edu.ubb.consolegamesales.backend.service.security.AuthenticationInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class OrderService {
    private final RedisService redisService;
    private final AnnouncementMapper announcementMapper;
    private final String kafkaOrderListOfUserReqTopic;
    private final KafkaTemplate<String, OrderListOfUserReqDto> kafkaTemplateOrderListReq;
    private final String kafkaOrderDataReqProduceTopic;
    private final KafkaTemplate<String, OrderDataReqDto> kafkaTemplateOrderDataReq;
    private final String kafkaOrderAnnouncementReqProduceTopic;
    private final KafkaTemplate<String, OrderAnnouncementReqDto> kafkaTemplateOrderAnnouncementReq;
    // order transactions
    private final String kafkaOrderTransactionOrderCreateProduceTopic;
    private final KafkaTemplate<String, TransactionOrderCreationDto> kafkaTemplateOrderTransactionOrderCreate;
    private final String kafkaOrderTransactionOrderDeleteProduceTopic;
    private final KafkaTemplate<String, TransactionOrderDeletionDto> kafkaTemplateOrderTransactionOrderDelete;


    public OrderService(
            RedisService redisService,
            AnnouncementMapper announcementMapper,
            @Value("${kafkaOrderListOfUserReqTopic}") String kafkaOrderListOfUserReqTopic,
            KafkaTemplate<String, OrderListOfUserReqDto> kafkaTemplateOrderListReq,
            @Value("${kafkaOrderDataReqProduceTopic}") String kafkaOrderDataReqProduceTopic,
            KafkaTemplate<String, OrderDataReqDto> kafkaTemplateOrderDataReq,
            @Value("${kafkaOrderAnnouncementReqProduceTopic}") String kafkaOrderAnnouncementReqProduceTopic,
            KafkaTemplate<String, OrderAnnouncementReqDto> kafkaTemplateOrderAnnouncementReq,

            @Value("${kafkaOrderTransactionOrderCreateProduceTopic}")
            String kafkaOrderTransactionOrderCreateProduceTopic,
            KafkaTemplate<String, TransactionOrderCreationDto> kafkaTemplateOrderTransactionOrderCreate,
            @Value("${kafkaOrderTransactionOrderDeleteProduceTopic}")
            String kafkaOrderTransactionOrderDeleteProduceTopic,
            KafkaTemplate<String, TransactionOrderDeletionDto> kafkaTemplateOrderTransactionOrderDelete
    ) {
        this.redisService = redisService;
        this.announcementMapper = announcementMapper;
        this.kafkaOrderListOfUserReqTopic = kafkaOrderListOfUserReqTopic;
        this.kafkaTemplateOrderListReq = kafkaTemplateOrderListReq;
        this.kafkaOrderDataReqProduceTopic = kafkaOrderDataReqProduceTopic;
        this.kafkaOrderAnnouncementReqProduceTopic = kafkaOrderAnnouncementReqProduceTopic;
        this.kafkaTemplateOrderDataReq = kafkaTemplateOrderDataReq;
        this.kafkaTemplateOrderAnnouncementReq = kafkaTemplateOrderAnnouncementReq;
        this.kafkaOrderTransactionOrderCreateProduceTopic = kafkaOrderTransactionOrderCreateProduceTopic;
        this.kafkaTemplateOrderTransactionOrderCreate = kafkaTemplateOrderTransactionOrderCreate;
        this.kafkaOrderTransactionOrderDeleteProduceTopic = kafkaOrderTransactionOrderDeleteProduceTopic;
        this.kafkaTemplateOrderTransactionOrderDelete = kafkaTemplateOrderTransactionOrderDelete;
    }

    public void requestOrdersOfBuyerPaginated(Long buyerId,
                                              int page, int limit) {
        LOGGER.info("Sending request of orders of user {} page {} limit {}", buyerId, page, limit);
        OrderListOfUserReqDto orderListOfUserReqDto = new OrderListOfUserReqDto(buyerId, page, limit);
        kafkaTemplateOrderListReq.send(kafkaOrderListOfUserReqTopic, buyerId.toString(), orderListOfUserReqDto);
    }

    public OrderListDto loadOrderByID(Long orderId, Authentication authentication) {
        User user = AuthenticationInformation.extractUser(authentication);
        Order order = redisService.getCachedOrder(orderId);
        if (order == null) {
            kafkaTemplateOrderDataReq.send(
                    kafkaOrderDataReqProduceTopic, orderId.toString(),
                    new OrderDataReqDto(orderId)
            );
            throw new DataUnderProcessingException();
        }
        if (!Objects.equals(user.getEntityId(), order.getBuyerId())) {
            throw new AccessDeniedException("You cannot access this resource!");
        }
        Announcement announcement = redisService.getCachedAnnouncement(order.getAnnouncementId());
        if (announcement == null) {
            kafkaTemplateOrderAnnouncementReq.send(
                    kafkaOrderAnnouncementReqProduceTopic, orderId.toString(),
                    new OrderAnnouncementReqDto(orderId, order)
            );
            throw new DataUnderProcessingException();
        }

        return new OrderListDto(
                order.getEntityId(), order.getOrderDate(),
                order.getPrice(), order.getOrderAddress(),
                announcementMapper.modelToListShortDto(announcement)
        );
    }

    public void createOrder(OrderCreationDto orderCreationDto, Authentication authentication) {
        User user = AuthenticationInformation.extractUser(authentication);
        if (!Objects.equals(user.getEntityId(), orderCreationDto.getBuyerId())) {
            throw new AccessDeniedException("You cannot order in the name of another user!");
        }
        kafkaTemplateOrderTransactionOrderCreate.send(
                kafkaOrderTransactionOrderCreateProduceTopic, orderCreationDto.getAnnouncementId().toString(),
                new TransactionOrderCreationDto(orderCreationDto)
        );
    }

    public void deleteOrderById(Long orderId, Authentication authentication) {
        User user = AuthenticationInformation.extractUser(authentication);
        kafkaTemplateOrderTransactionOrderDelete.send(
                kafkaOrderTransactionOrderDeleteProduceTopic, orderId.toString(),
                new TransactionOrderDeletionDto(orderId, user)
        );
    }
}
