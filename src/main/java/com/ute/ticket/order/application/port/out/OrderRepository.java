package com.ute.ticket.order.application.port.out;

import com.ute.ticket.order.domain.entity.Order;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findWithItemsById(Long id);
}