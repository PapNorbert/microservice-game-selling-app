package edu.ubb.consolegamesales.backend.repository;

import edu.ubb.consolegamesales.backend.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepository extends Repository<Order, Long> {
    Page<Order> findAllByBuyerEntityId(Long buyerId, Pageable pageable);

}
