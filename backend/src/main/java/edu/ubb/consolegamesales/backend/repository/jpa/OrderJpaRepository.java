package edu.ubb.consolegamesales.backend.repository.jpa;


import edu.ubb.consolegamesales.backend.model.Order;
import edu.ubb.consolegamesales.backend.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("jpa")
public interface OrderJpaRepository extends OrderRepository, JpaRepository<Order, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Order "
            + "SET price= :#{#entity.price} "
            + " WHERE entityId = :id ")
    @Override
    Integer update(Long id, Order entity);

    Page<Order> findAllByBuyerEntityId(Long buyerId, Pageable pageable);

    @Override
    Optional<Order> findByEntityId(Long orderId);

}
