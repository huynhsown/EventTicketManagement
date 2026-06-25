package com.ute.ticket.event.infrastructure.persistence.jpa.mapper;

import com.ute.ticket.event.domain.entity.Session;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.SessionJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    public Session toDomain(SessionJpaEntity entity) {
        return Session.builder()
                .id(entity.getId())
                .eventId(entity.getEventId())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .salesStartAt(entity.getSalesStartAt())
                .salesEndAt(entity.getSalesEndAt())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .version(entity.getVersion())
                .build();
    }

    public SessionJpaEntity toJpaEntity(Session domain) {
        return SessionJpaEntity.builder()
                .id(domain.getId())
                .eventId(domain.getEventId())
                .startTime(domain.getStartTime())
                .endTime(domain.getEndTime())
                .salesStartAt(domain.getSalesStartAt())
                .salesEndAt(domain.getSalesEndAt())
                .status(domain.getStatus())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedBy(domain.getUpdatedBy())
                .deletedAt(domain.getDeletedAt())
                .version(domain.getVersion())
                .build();
    }

    public void updateEntity(SessionJpaEntity entity, Session domain) {
        entity.setStartTime(domain.getStartTime());
        entity.setEndTime(domain.getEndTime());
        entity.setSalesStartAt(domain.getSalesStartAt());
        entity.setSalesEndAt(domain.getSalesEndAt());
        entity.setStatus(domain.getStatus());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setVersion(domain.getVersion());
    }
}
