package com.ute.ticket.reservation.infrastructure.persistence.jpa.repository;

import com.ute.ticket.reservation.domain.enums.ReservationStatus;
import com.ute.ticket.reservation.infrastructure.persistence.jpa.entity.ReservationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReservationJpaRepository extends JpaRepository<ReservationJpaEntity, UUID> {

    List<ReservationJpaEntity> findAllByStatusAndExpiresAtBefore(ReservationStatus status, Instant expiresAt);
}
