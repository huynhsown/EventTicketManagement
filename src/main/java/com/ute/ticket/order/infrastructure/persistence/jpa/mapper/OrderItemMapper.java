package com.ute.ticket.order.infrastructure.persistence.jpa.mapper;

import com.ute.ticket.order.domain.entity.OrderItem;
import com.ute.ticket.order.infrastructure.persistence.jpa.entity.OrderItemJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItem toDomain(OrderItemJpaEntity entity) {
        return OrderItem.builder()
                .id(entity.getId())
                .orderId(entity.getOrderId())
                .ticketTypeId(entity.getTicketTypeId())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .subtotal(entity.getSubtotal())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .version(entity.getVersion())
                .build();
    }

    public OrderItemJpaEntity toJpaEntity(OrderItem domain) {
        return OrderItemJpaEntity.builder()
                .id(domain.getId())
                .orderId(domain.getOrderId())
                .ticketTypeId(domain.getTicketTypeId())
                .quantity(domain.getQuantity())
                .unitPrice(domain.getUnitPrice())
                .subtotal(domain.getSubtotal())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedBy(domain.getUpdatedBy())
                .deletedAt(domain.getDeletedAt())
                .version(domain.getVersion())
                .build();
    }

    public void updateEntity(OrderItemJpaEntity entity, OrderItem domain) {
        entity.setOrderId(domain.getOrderId());
        entity.setTicketTypeId(domain.getTicketTypeId());
        entity.setQuantity(domain.getQuantity());
        entity.setUnitPrice(domain.getUnitPrice());
        entity.setSubtotal(domain.getSubtotal());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setVersion(domain.getVersion());
    }
}
