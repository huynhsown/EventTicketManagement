package com.ute.ticket.event.infrastructure.persistence.jpa.mapper;

import com.ute.ticket.event.domain.entity.InventoryTransaction;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.InventoryTransactionJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class InventoryTransactionMapper {

    public InventoryTransaction toDomain(InventoryTransactionJpaEntity entity) {
        return InventoryTransaction.builder()
                .id(entity.getId())
                .ticketTypeId(entity.getTicketTypeId())
                .type(entity.getType())
                .quantity(entity.getQuantity())
                .referenceId(entity.getReferenceId())
                .note(entity.getNote())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .version(entity.getVersion())
                .build();
    }

    public InventoryTransactionJpaEntity toJpaEntity(InventoryTransaction domain) {
        return InventoryTransactionJpaEntity.builder()
                .id(domain.getId())
                .ticketTypeId(domain.getTicketTypeId())
                .type(domain.getType())
                .quantity(domain.getQuantity())
                .referenceId(domain.getReferenceId())
                .note(domain.getNote())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedBy(domain.getUpdatedBy())
                .deletedAt(domain.getDeletedAt())
                .version(domain.getVersion())
                .build();
    }
}
