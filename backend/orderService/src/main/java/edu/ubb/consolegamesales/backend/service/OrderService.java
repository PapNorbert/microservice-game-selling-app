package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.controller.mapper.OrderMapper;
import edu.ubb.consolegamesales.backend.dto.kafka.OrdersListAnnouncementsReqDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.Pagination;
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
    private final OrderMapper orderMapper;
    private final RedisService redisService;
    private final static int TIME_ALLOWED_FOR_ORDER_CANCEL_IN_MILI_SEC = 8640000;
    private final String kafkaOrdersListAnnouncementsReqTopic;
    private final KafkaTemplate<String, OrdersListAnnouncementsReqDto> kafkaOrdersListAnnouncementsTemplate;

    public OrderService(OrderRepository orderRepository,
                        OrderMapper orderMapper,
                        RedisService redisService,
                        @Value("${kafkaOrdersListAnnouncementsReq}") String kafkaOrdersListAnnouncementsReqTopic,
                        KafkaTemplate<String, OrdersListAnnouncementsReqDto> kafkaOrdersListAnnouncementsTemplate

    ) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.redisService = redisService;
        this.kafkaOrdersListAnnouncementsReqTopic = kafkaOrdersListAnnouncementsReqTopic;
        this.kafkaOrdersListAnnouncementsTemplate = kafkaOrdersListAnnouncementsTemplate;
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

//    public OrderListDto findOrderById(Long orderId, Authentication authentication) {
//        try {
//            User user = AuthenticationInformation.extractUser(authentication);
//            Order order = loadOrderById(orderId, user);
//            redisService.storeOrderInCache(orderId, order);
//            return orderMapper.modelToOrderListDto(order);
//        } catch (EntityNotFoundException e) {
//            throw new NotFoundException("Order with ID " + orderId + " not found", e);
//        }
//    }


//    public CreatedObjectDto createOrder(OrderCreationDto orderCreationDto,
//                                        Authentication authentication) {
//        User user = AuthenticationInformation.extractUser(authentication);
//        if (!Objects.equals(user.getEntityId(), orderCreationDto.getBuyerId())) {
//            throw new AccessDeniedException("You cannot order in the name of another user!");
//        }
//
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
//        Order order = orderRepository.saveAndFlush(
//                orderMapper.creationDtoToModel(orderCreationDto));
//        AnnouncementEvent announcementEvent = new AnnouncementEvent(
//                announcement, "Announcement sold, order created", new Date(), user);
//        announcementEventRepository.saveAndFlush(announcementEvent);
//
//        return orderMapper.modelToCreatedObjDto(order);
//    }
//
//    public void deleteOrderById(Long id, Authentication authentication) {
//        User user = AuthenticationInformation.extractUser(authentication);
//        Order order = loadOrderById(id, user);
//        // order exists and logged-in user owns it
//
//        if (new Date().getTime() - order.getOrderDate().getTime() > TIME_ALLOWED_FOR_ORDER_CANCEL_IN_MILI_SEC) {
//            throw new AccessDeniedException("You cannot cancel the order, too much time passed");
//        }
//        orderRepository.delete(order);
//        // update announcement
//        Announcement announcement = announcementRepository.getById(order.getAnnouncement().getEntityId());
//        announcement.setSold(false);
//        announcementRepository.update(announcement.getEntityId(), announcement);
//        AnnouncementEvent announcementEvent = new AnnouncementEvent(
//                announcement, "Announcement order canceled, announcement not sold", new Date(), user);
//        announcementEventRepository.saveAndFlush(announcementEvent);
//        // update cache
//        redisService.deleteCachedAnnouncement(announcement.getEntityId());
//        redisService.deleteCachedOrder(id);
//    }


//    private Order loadOrderById(Long orderId, User user)
//            throws EntityNotFoundException, AccessDeniedException {
//        Order order = redisService.getCachedOrder(orderId);
//        if (order == null) {
//            order = orderRepository.findByEntityId(orderId).orElseThrow(
//                    () -> new NotFoundException("Order with ID " + orderId + " not found"));
//        }
//        // check order of the user
//        if (!Objects.equals(user.getEntityId(), order.getBuyerId())) {
//            throw new AccessDeniedException("You cannot access this resource!");
//        }
//        return order;
//    }
}
