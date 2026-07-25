package com.ute.ticket.reservation.application.port.out;

import com.ute.ticket.reservation.domain.entity.Reservation;
import com.ute.ticket.reservation.domain.enums.ReservationStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(UUID id);
    boolean existsById(UUID id);
    List<Reservation> findAllByStatusAndExpiresAtBefore(ReservationStatus status, Instant expiresAt);
}
