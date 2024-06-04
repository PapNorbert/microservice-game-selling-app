package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.dto.kafka.OrderAnnouncementReqDto;
import edu.ubb.consolegamesales.backend.dto.kafka.OrderResponseDto;
import edu.ubb.consolegamesales.backend.dto.kafka.OrdersListAnnouncementsReqDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.Pagination;
import edu.ubb.consolegamesales.backend.model.Announcement;
import edu.ubb.consolegamesales.backend.model.Order;
import edu.ubb.consolegamesales.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final RedisService redisService;
    private final String kafkaOrdersListAnnouncementsReqTopic;
    private final KafkaTemplate<String, OrdersListAnnouncementsReqDto> kafkaOrdersListAnnouncementsTemplate;
    private final String kafkaOrderAnnouncementReqProduceTopic;
    private final KafkaTemplate<String, OrderAnnouncementReqDto> kafkaTemplateOrderAnnouncementReq;
    private final String kafkaOrderResponseProduceTopic;
    private final KafkaTemplate<String, OrderResponseDto> kafkaTemplateOrderResponse;


    public OrderService(OrderRepository orderRepository,
                        RedisService redisService,
                        @Value("${kafkaOrdersListAnnouncementsReq}") String kafkaOrdersListAnnouncementsReqTopic,
                        KafkaTemplate<String, OrdersListAnnouncementsReqDto> kafkaOrdersListAnnouncementsTemplate,
                        @Value("${kafkaOrderAnnouncementReqProduceTopic}") String kafkaOrderAnnouncementReqProduceTopic,
                        KafkaTemplate<String, OrderAnnouncementReqDto> kafkaTemplateOrderAnnouncementReq,
                        @Value("${kafkaOrderResponseProduceTopic}") String kafkaOrderResponseProduceTopic,
                        KafkaTemplate<String, OrderResponseDto> kafkaTemplateOrderResponse
    ) {
        this.orderRepository = orderRepository;
        this.redisService = redisService;
        this.kafkaOrdersListAnnouncementsReqTopic = kafkaOrdersListAnnouncementsReqTopic;
        this.kafkaOrdersListAnnouncementsTemplate = kafkaOrdersListAnnouncementsTemplate;
        this.kafkaOrderAnnouncementReqProduceTopic = kafkaOrderAnnouncementReqProduceTopic;
        this.kafkaTemplateOrderAnnouncementReq = kafkaTemplateOrderAnnouncementReq;
        this.kafkaOrderResponseProduceTopic = kafkaOrderResponseProduceTopic;
        this.kafkaTemplateOrderResponse = kafkaTemplateOrderResponse;
    }


    public void findAllOrdersOfUser(Long userId, int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit,
                Sort.by("orderDate"));
        Page<Order> orderPage = orderRepository.findAllByBuyerId(userId, pageRequest);
        Pagination pagination = new Pagination(page, limit,
                orderPage.getTotalElements(), orderPage.getTotalPages());
        List<Order> orders = orderPage.getContent();
        OrdersListAnnouncementsReqDto ordersListAnnouncementsReqDto =
                new OrdersListAnnouncementsReqDto(userId, pagination, orders);
        kafkaOrdersListAnnouncementsTemplate.send(
                kafkaOrdersListAnnouncementsReqTopic, userId.toString(), ordersListAnnouncementsReqDto);
    }

    public void findOrderById(Long orderId) {
        Order order = loadOrderById(orderId);
        if (order == null) {
            // order not found
            kafkaTemplateOrderResponse.send(
                    kafkaOrderResponseProduceTopic, orderId.toString(),
                    new OrderResponseDto(orderId, null, null)
            );
            return;
        }
        Announcement announcement = redisService.getCachedAnnouncement(order.getAnnouncementId());
        if (announcement == null) {
            // send request to announcements service for announcement data
            kafkaTemplateOrderAnnouncementReq.send(
                    kafkaOrderAnnouncementReqProduceTopic, orderId.toString(),
                    new OrderAnnouncementReqDto(orderId, order)
            );
            return;
        }
        // we have all the data, send to websocket to send response
        kafkaTemplateOrderResponse.send(
                kafkaOrderResponseProduceTopic, orderId.toString(),
                new OrderResponseDto(orderId, order, announcement)
        );
    }

    // TODO AnnouncementEvents create/delete

//    public CreatedObjectDto createOrder(OrderCreationDto orderCreationDto,
//                                        Authentication authentication) {

//        Announcement announcement = announcementRepository.getById(orderCreationDto.getAnnouncementId());
//        if (announcement == null) {
//            throw new NotFoundException("No announcement found for id " + orderCreationDto.getAnnouncementId());
//        }
//        if (announcement.getSold()) {
//            throw new AnnouncementAlreadySoldException();
//        }
//        announcement.setSold(true);
//        announcementRepository.update(announcement.getEntityId(), announcement);
//        redisService.deleteCachedAnnouncement(announcement.getEntityId());

//        AnnouncementEvent announcementEvent = new AnnouncementEvent(
//                announcement, "Announcement sold, order created", new Date(), user);
//        announcementEventRepository.saveAndFlush(announcementEvent);
//
//        return orderMapper.modelToCreatedObjDto(order);
//    }
//
//    public void deleteOrderById(Long id, Authentication authentication) {
//        // update announcement
//        Announcement announcement = announcementRepository.getById(order.getAnnouncement().getEntityId());
//        announcement.setSold(false);
//        announcementRepository.update(announcement.getEntityId(), announcement);
//        AnnouncementEvent announcementEvent = new AnnouncementEvent(
//                announcement, "Announcement order canceled, announcement not sold", new Date(), user);
//        announcementEventRepository.saveAndFlush(announcementEvent);
//        // update cache
//        redisService.deleteCachedAnnouncement(announcement.getEntityId());
//    }


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
