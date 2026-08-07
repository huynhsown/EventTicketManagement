package com.ute.ticket.order.infrastructure.persistence.jpa.mapper;

import com.ute.ticket.order.domain.entity.Order;
import com.ute.ticket.order.domain.entity.OrderItem;
import com.ute.ticket.order.infrastructure.persistence.jpa.entity.OrderItemJpaEntity;
import com.ute.ticket.order.infrastructure.persistence.jpa.entity.OrderJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    public Order toFullDomain(OrderJpaEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(orderItemMapper::toDomain)
                .toList();
        Order order = Order.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .reservationId(entity.getReservationId())
                .userId(entity.getUserId())
                .status(entity.getStatus())
                .totalAmount(entity.getTotalAmount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .version(entity.getVersion())
                .build();
        order.addItems(items);
        return order;
    }

    public Order toDomain(OrderJpaEntity entity) {
        return Order.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .reservationId(entity.getReservationId())
                .userId(entity.getUserId())
                .status(entity.getStatus())
                .totalAmount(entity.getTotalAmount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .version(entity.getVersion())
                .build();
    }

    public OrderJpaEntity toJpaEntity(Order domain) {
        return OrderJpaEntity.builder()
                .id(domain.getId())
                .code(domain.getCode())
                .reservationId(domain.getReservationId())
                .userId(domain.getUserId())
                .status(domain.getStatus())
                .totalAmount(domain.getTotalAmount())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedBy(domain.getUpdatedBy())
                .deletedAt(domain.getDeletedAt())
                .version(domain.getVersion())
                .build();
    }

    public void updateFull(OrderJpaEntity entity, Order domain) {
        updateOnlyOrderJpa(entity, domain);
        mergeItems(entity, domain);
    }

    public void updateOnlyOrderJpa(OrderJpaEntity entity, Order domain) {
        entity.setCode(domain.getCode());
        entity.setReservationId(domain.getReservationId());
        entity.setUserId(domain.getUserId());
        entity.setStatus(domain.getStatus());
        entity.setTotalAmount(domain.getTotalAmount());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setVersion(domain.getVersion());
    }

    private void mergeItems(OrderJpaEntity entity, Order domain) {
        if (domain.getItems() == null) {
            entity.getItems().clear();
            return;
        }

        Set<Long> domainItemIds = domain.getItems().stream()
                .map(OrderItem::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        entity.getItems().removeIf(item -> !domainItemIds.contains(item.getId()));

        Map<Long, OrderItemJpaEntity> existingById = entity.getItems().stream()
                .collect(Collectors.toMap(OrderItemJpaEntity::getId, Function.identity()));

        for (OrderItem domainItem : domain.getItems()) {
            if (domainItem.getId() != null && existingById.containsKey(domainItem.getId())) {
                orderItemMapper.updateEntity(existingById.get(domainItem.getId()), domainItem);
            } else {
                OrderItemJpaEntity jpaItem = orderItemMapper.toJpaEntity(domainItem);
                jpaItem.setOrder(entity);
                entity.getItems().add(jpaItem);
            }
        }
    }
}