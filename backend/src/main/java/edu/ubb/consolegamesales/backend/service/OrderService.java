package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.OrderCreationDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.OrderListDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.OrderListWithPaginationDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.Pagination;
import edu.ubb.consolegamesales.backend.controller.exception.NotFoundException;
import edu.ubb.consolegamesales.backend.controller.mapper.OrderMapper;
import edu.ubb.consolegamesales.backend.model.Announcement;
import edu.ubb.consolegamesales.backend.model.AnnouncementEvent;
import edu.ubb.consolegamesales.backend.model.Order;
import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.repository.AnnouncementEventRepository;
import edu.ubb.consolegamesales.backend.repository.AnnouncementRepository;
import edu.ubb.consolegamesales.backend.repository.OrderRepository;
import edu.ubb.consolegamesales.backend.service.exception.AnnouncementAlreadySoldException;
import edu.ubb.consolegamesales.backend.service.security.AuthenticationInformation;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AnnouncementRepository announcementRepository;
    private final RedisService redisService;
    private final AnnouncementEventRepository announcementEventRepository;
    private final static int TIME_ALLOWED_FOR_ORDER_CANCEL_IN_MILI_SEC = 8640000;

    public OrderListWithPaginationDto findAllOrdersOfUser(Long userId, int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit,
                Sort.by("orderDate"));
        Page<Order> orderPage = orderRepository.findAllByBuyerEntityId(userId, pageRequest);
        List<OrderListDto> bookingListingDtos = orderMapper.modelsToOrderListDtos(
                orderPage.getContent());
        Pagination pagination = new Pagination(page, limit,
                orderPage.getTotalElements(), orderPage.getTotalPages());
        return new OrderListWithPaginationDto(bookingListingDtos, pagination);
    }

    public OrderListDto findOrderById(Long orderId, Authentication authentication) {
        try {
            User user = AuthenticationInformation.extractUser(authentication);
            Order order = loadOrderById(orderId, user);
            redisService.storeOrderInCache(orderId, order);
            return orderMapper.modelToOrderListDto(order);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Order with ID " + orderId + " not found", e);
        }
    }


    public CreatedObjectDto createOrder(OrderCreationDto orderCreationDto,
                                        Authentication authentication) {
        User user = AuthenticationInformation.extractUser(authentication);
        if (!Objects.equals(user.getEntityId(), orderCreationDto.getBuyerId())) {
            throw new AccessDeniedException("You cannot order in the name of another user!");
        }

        Announcement announcement = announcementRepository.getById(orderCreationDto.getAnnouncementId());
        if (announcement == null) {
            throw new NotFoundException("No announcement found for id " + orderCreationDto.getAnnouncementId());
        }
        if (announcement.getSold()) {
            throw new AnnouncementAlreadySoldException();
        }
        announcement.setSold(true);
        announcementRepository.update(announcement.getEntityId(), announcement);
        redisService.deleteCachedAnnouncement(announcement.getEntityId());
        Order order = orderRepository.saveAndFlush(
                orderMapper.creationDtoToModel(orderCreationDto));
        AnnouncementEvent announcementEvent = new AnnouncementEvent(
                announcement, "Announcement sold, order created", new Date(), user);
        announcementEventRepository.saveAndFlush(announcementEvent);

        return orderMapper.modelToCreatedObjDto(order);
    }

    public void deleteOrderById(Long id, Authentication authentication) {
        User user = AuthenticationInformation.extractUser(authentication);
        Order order = loadOrderById(id, user);
        // order exists and logged-in user owns it

        if (new Date().getTime() - order.getOrderDate().getTime() > TIME_ALLOWED_FOR_ORDER_CANCEL_IN_MILI_SEC) {
            throw new AccessDeniedException("You cannot cancel the order, too much time passed");
        }
        orderRepository.delete(order);
        // update announcement
        Announcement announcement = announcementRepository.getById(order.getAnnouncement().getEntityId());
        announcement.setSold(false);
        announcementRepository.update(announcement.getEntityId(), announcement);
        AnnouncementEvent announcementEvent = new AnnouncementEvent(
                announcement, "Announcement order canceled, announcement not sold", new Date(), user);
        announcementEventRepository.saveAndFlush(announcementEvent);
        // update cache
        redisService.deleteCachedAnnouncement(announcement.getEntityId());
        redisService.deleteCachedOrder(id);
    }


    private Order loadOrderById(Long orderId, User user)
            throws EntityNotFoundException, AccessDeniedException {
        Order order = redisService.getCachedOrder(orderId);
        if (order == null) {
            order = orderRepository.findByEntityId(orderId).orElseThrow(
                    () -> new NotFoundException("Order with ID " + orderId + " not found"));
        }
        // check order of the user
        if (!Objects.equals(user.getEntityId(), order.getBuyer().getEntityId())) {
            throw new AccessDeniedException("You cannot access this resource!");
        }
        return order;
    }
}
