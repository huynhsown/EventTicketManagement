package com.ute.ticket.event.infrastructure.persistence.jpa.mapper;

import com.ute.ticket.event.domain.entity.Inventory;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.InventoryJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public Inventory toDomain(InventoryJpaEntity entity) {
        return Inventory.builder()
                .ticketTypeId(entity.getTicketTypeId())
                .totalStock(entity.getTotalStock())
                .reservedStock(entity.getReservedStock())
                .soldStock(entity.getSoldStock())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .version(entity.getVersion())
                .build();
    }

    public InventoryJpaEntity toJpaEntity(Inventory domain) {
        return InventoryJpaEntity.builder()
                .ticketTypeId(domain.getTicketTypeId())
                .totalStock(domain.getTotalStock())
                .reservedStock(domain.getReservedStock())
                .soldStock(domain.getSoldStock())
                .status(domain.getStatus())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedBy(domain.getUpdatedBy())
                .deletedAt(domain.getDeletedAt())
                .version(domain.getVersion())
                .build();
    }

    public void updateEntity(InventoryJpaEntity entity, Inventory domain) {
        entity.setTotalStock(domain.getTotalStock());
        entity.setReservedStock(domain.getReservedStock());
        entity.setSoldStock(domain.getSoldStock());
        entity.setStatus(domain.getStatus());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setVersion(domain.getVersion());
    }
}
