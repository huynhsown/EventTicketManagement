package com.ute.ticket.order.infrastructure.persistence.adapter;

import com.ute.ticket.order.application.port.out.OrderRepository;
import com.ute.ticket.order.domain.entity.Order;
import com.ute.ticket.order.infrastructure.persistence.jpa.entity.OrderJpaEntity;
import com.ute.ticket.order.infrastructure.persistence.jpa.mapper.OrderMapper;
import com.ute.ticket.order.infrastructure.persistence.jpa.repository.OrderJpaRepository;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderMapper orderMapper;

    @Override
    public Order save(Order order) {
        OrderJpaEntity entity;
        if (order.getId() == null || order.getVersion() == null) {
            entity = orderMapper.toJpaEntity(order);
            orderMapper.updateFull(entity, order);
        } else {
            entity = orderJpaRepository.findById(order.getId())
                    .orElseThrow(() -> new NotFoundException("Order not found"));
            orderMapper.updateFull(entity, order);
        }
        OrderJpaEntity saved = orderJpaRepository.save(entity);
        return orderMapper.toFullDomain(saved);
    }

    @Override
    public Optional<Order> findWithItemsById(Long id) {
        return orderJpaRepository.findWithItemsById(id)
                .map(orderMapper::toFullDomain);
    }
}