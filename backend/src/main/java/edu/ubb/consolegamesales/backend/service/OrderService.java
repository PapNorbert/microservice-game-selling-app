package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.OrderCreationDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.OrderListDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.OrderListWithPaginationDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.Pagination;
import edu.ubb.consolegamesales.backend.controller.exception.NotFoundException;
import edu.ubb.consolegamesales.backend.controller.mapper.OrderMapper;
import edu.ubb.consolegamesales.backend.model.Announcement;
import edu.ubb.consolegamesales.backend.model.Order;
import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.repository.AnnouncementRepository;
import edu.ubb.consolegamesales.backend.repository.OrderRepository;
import edu.ubb.consolegamesales.backend.service.exception.AnnouncementAlreadySoldException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AnnouncementRepository announcementRepository;


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

    public OrderListDto findOrderById(Long orderId) {
        try {
            OrderListDto orderListDto = orderMapper.modelToOrderListDto(
                    orderRepository.getById(orderId));
            if (orderListDto == null) {
                throw new NotFoundException("Order with ID " + orderId + " not found");
            } else {
                return orderListDto;
            }
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Order with ID " + orderId + " not found", e);
        }
    }

    public CreatedObjectDto createOrder(OrderCreationDto orderCreationDto,
                                        Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal() == null) {
            throw new AccessDeniedException("You must Login first to access this resource!");
        }
        User user = (User) authentication.getPrincipal();
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
        Order order = orderRepository.saveAndFlush(
                orderMapper.creationDtoToModel(orderCreationDto));
        return orderMapper.modelToCreatedObjDto(order);
    }
}
