package com.ute.ticket.event.infrastructure.persistence.jpa.mapper;

import com.ute.ticket.event.domain.entity.TicketType;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.TicketTypeJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TicketTypeMapper {

    public TicketType toDomain(TicketTypeJpaEntity entity) {
        return TicketType.builder()
                .id(entity.getId())
                .sessionId(entity.getSessionId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .maxPerUser(entity.getMaxPerUser())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .version(entity.getVersion())
                .build();
    }

    public TicketTypeJpaEntity toJpaEntity(TicketType domain) {
        return TicketTypeJpaEntity.builder()
                .id(domain.getId())
                .sessionId(domain.getSessionId())
                .name(domain.getName())
                .description(domain.getDescription())
                .price(domain.getPrice())
                .maxPerUser(domain.getMaxPerUser())
                .status(domain.getStatus())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedBy(domain.getUpdatedBy())
                .deletedAt(domain.getDeletedAt())
                .version(domain.getVersion())
                .build();
    }

    public void updateEntity(TicketTypeJpaEntity entity, TicketType domain) {
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setPrice(domain.getPrice());
        entity.setMaxPerUser(domain.getMaxPerUser());
        entity.setStatus(domain.getStatus());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setVersion(domain.getVersion());
    }
}
