package com.ute.ticket.reservation.infrastructure.persistence.jpa.mapper;

import com.ute.ticket.reservation.domain.entity.Reservation;
import com.ute.ticket.reservation.infrastructure.persistence.jpa.entity.ReservationJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public Reservation toDomain(ReservationJpaEntity entity) {
        return Reservation.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .ticketTypeId(entity.getTicketTypeId())
                .quantity(entity.getQuantity())
                .status(entity.getStatus())
                .expiresAt(entity.getExpiresAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .version(entity.getVersion())
                .build();
    }

    public ReservationJpaEntity toJpaEntity(Reservation domain) {
        return ReservationJpaEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .ticketTypeId(domain.getTicketTypeId())
                .quantity(domain.getQuantity())
                .status(domain.getStatus())
                .expiresAt(domain.getExpiresAt())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedBy(domain.getUpdatedBy())
                .deletedAt(domain.getDeletedAt())
                .version(domain.getVersion())
                .build();
    }

    public void updateEntity(ReservationJpaEntity entity, Reservation domain) {
        entity.setUserId(domain.getUserId());
        entity.setTicketTypeId(domain.getTicketTypeId());
        entity.setQuantity(domain.getQuantity());
        entity.setStatus(domain.getStatus());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setVersion(domain.getVersion());
    }
}
