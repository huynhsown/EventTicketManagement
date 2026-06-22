package com.ute.ticket.event.infrastructure.persistence.jpa.mapper;

import com.ute.ticket.event.domain.entity.EventCategory;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.EventCategoryJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class EventCategoryMapper {

    public EventCategory toDomain(EventCategoryJpaEntity entity) {
        return EventCategory.builder()
                .eventId(entity.getEventId())
                .categoryId(entity.getCategoryId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .version(entity.getVersion())
                .build();
    }

    public EventCategoryJpaEntity toJpaEntity(EventCategory domain) {
        return EventCategoryJpaEntity.builder()
                .eventId(domain.getEventId())
                .categoryId(domain.getCategoryId())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedBy(domain.getUpdatedBy())
                .deletedAt(domain.getDeletedAt())
                .version(domain.getVersion())
                .build();
    }
}
