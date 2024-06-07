package edu.ubb.consolegamesales.backend.repository;

import edu.ubb.consolegamesales.backend.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderRepository extends Repository<Order, Long> {
    Page<Order> findAllByBuyerId(Long buyerId, Pageable pageable);

    Optional<Order> findByEntityId(Long orderId);
}
