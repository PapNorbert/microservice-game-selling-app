package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.controller.mapper.OrderMapper;
import edu.ubb.consolegamesales.backend.dto.incoming.OrderCreationDto;
import edu.ubb.consolegamesales.backend.dto.kafka.TransactionOrderCreateRespDto;
import edu.ubb.consolegamesales.backend.dto.kafka.TransactionOrderCreationDto;
import edu.ubb.consolegamesales.backend.dto.kafka.TransactionOrderDeleteRespDto;
import edu.ubb.consolegamesales.backend.dto.kafka.TransactionOrderDeletionDto;
import edu.ubb.consolegamesales.backend.model.Order;
import edu.ubb.consolegamesales.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class OrderTransactionService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final RedisService redisService;
    private final static int TIME_ALLOWED_FOR_ORDER_CANCEL_IN_MILI_SEC = 8640000;
    //    order creation topic
    private final String kafkaOrderTransactionOrderCreateProduceTopic;
    private final KafkaTemplate<String, TransactionOrderCreateRespDto> kafkaOrderTransactionCreateResponseTemplate;
    //    order deletion topic
    private final String kafkaOrderTransactionOrderDeleteProduceTopic;
    private final KafkaTemplate<String, TransactionOrderDeleteRespDto> kafkaOrderTransactionDeleteResponseTemplate;

    public OrderTransactionService(
            OrderRepository orderRepository,
            OrderMapper orderMapper,
            RedisService redisService,
            @Value("${kafkaOrderTransactionOrderCreateProduceTopic}")
            String kafkaOrderTransactionOrderCreateProduceTopic,
            KafkaTemplate<String, TransactionOrderCreateRespDto> kafkaOrderTransactionCreateResponseTemplate,
            @Value("${kafkaOrderTransactionOrderDeleteProduceTopic}")
            String kafkaOrderTransactionOrderDeleteProduceTopic,
            KafkaTemplate<String, TransactionOrderDeleteRespDto> kafkaOrderTransactionDeleteResponseTemplate
    ) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.redisService = redisService;
        this.kafkaOrderTransactionOrderCreateProduceTopic = kafkaOrderTransactionOrderCreateProduceTopic;
        this.kafkaOrderTransactionCreateResponseTemplate = kafkaOrderTransactionCreateResponseTemplate;
        this.kafkaOrderTransactionOrderDeleteProduceTopic = kafkaOrderTransactionOrderDeleteProduceTopic;
        this.kafkaOrderTransactionDeleteResponseTemplate = kafkaOrderTransactionDeleteResponseTemplate;
    }

    public void createOrder(TransactionOrderCreationDto transactionOrderCreationDto) {
        OrderCreationDto orderCreationDto = transactionOrderCreationDto.getOrderCreationDto();
        Long announcementId = orderCreationDto.getAnnouncementId();
        try {
            Order order = orderRepository.saveAndFlush(orderMapper.creationDtoToModel(orderCreationDto));
            kafkaOrderTransactionCreateResponseTemplate.send(
                    kafkaOrderTransactionOrderCreateProduceTopic, announcementId.toString(),
                    new TransactionOrderCreateRespDto(announcementId, order,
                            true, orderCreationDto.getBuyerId())
            );
        } catch (Exception e) {
            // if any exception is thrown during saving the transaction fails
            kafkaOrderTransactionCreateResponseTemplate.send(
                    kafkaOrderTransactionOrderCreateProduceTopic, announcementId.toString(),
                    new TransactionOrderCreateRespDto(announcementId, null,
                            false, orderCreationDto.getBuyerId())
            );
        }
    }


    public void deleteOrder(TransactionOrderDeletionDto transactionOrderDeletionDto) {
        Long orderId = transactionOrderDeletionDto.getOrderId();
        Order order = loadOrderById(orderId);
        if (order == null) {
            // order not found
            order = new Order();
            order.setEntityId(orderId);
            kafkaOrderTransactionDeleteResponseTemplate.send(
                    kafkaOrderTransactionOrderDeleteProduceTopic, orderId.toString(),
                    new TransactionOrderDeleteRespDto(
                            order, null, false,
                            transactionOrderDeletionDto.getUser().getEntityId())
            );
            return;
        }
        if (!Objects.equals(order.getBuyerId(), transactionOrderDeletionDto.getUser().getEntityId())) {
            // order of another user
            kafkaOrderTransactionDeleteResponseTemplate.send(
                    kafkaOrderTransactionOrderDeleteProduceTopic, order.getAnnouncementId().toString(),
                    new TransactionOrderDeleteRespDto(
                            order, order.getAnnouncementId(), false,
                            transactionOrderDeletionDto.getUser().getEntityId())
            );
            return;
        }

        if (new Date().getTime() - order.getOrderDate().getTime() > TIME_ALLOWED_FOR_ORDER_CANCEL_IN_MILI_SEC) {
            // order cancelation too late
            kafkaOrderTransactionDeleteResponseTemplate.send(
                    kafkaOrderTransactionOrderDeleteProduceTopic, order.getAnnouncementId().toString(),
                    new TransactionOrderDeleteRespDto(
                            order, order.getAnnouncementId(), false,
                            transactionOrderDeletionDto.getUser().getEntityId())
            );
            return;
        }
        try {
            orderRepository.delete(order);
            redisService.deleteCachedOrder(order.getEntityId());
            kafkaOrderTransactionDeleteResponseTemplate.send(
                    kafkaOrderTransactionOrderDeleteProduceTopic, order.getAnnouncementId().toString(),
                    new TransactionOrderDeleteRespDto(
                            order, order.getAnnouncementId(), true,
                            transactionOrderDeletionDto.getUser().getEntityId())
            );
        } catch (Exception e) {
            // if any exception is thrown during delete the transaction fails
            kafkaOrderTransactionDeleteResponseTemplate.send(
                    kafkaOrderTransactionOrderDeleteProduceTopic, order.getAnnouncementId().toString(),
                    new TransactionOrderDeleteRespDto(
                            order, order.getAnnouncementId(), false,
                            transactionOrderDeletionDto.getUser().getEntityId())
            );
        }

    }

    private Order loadOrderById(Long orderId) {
        Order order = redisService.getCachedOrder(orderId);
        if (order == null) {
            order = orderRepository.findByEntityId(orderId).orElse(null);
            if (order != null) {
                redisService.storeOrderInCache(order.getEntityId(), order);
            }
        }
        return order;
    }
}