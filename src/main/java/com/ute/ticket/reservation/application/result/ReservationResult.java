package com.ute.ticket.reservation.application.result;

import com.ute.ticket.reservation.domain.entity.Reservation;
import com.ute.ticket.reservation.domain.enums.ReservationStatus;

import java.time.Instant;
import java.util.UUID;

public record ReservationResult(
        UUID id,
        Long userId,
        Long ticketTypeId,
        Integer quantity,
        ReservationStatus status,
        Instant expiresAt
) {
    public static ReservationResult from(Reservation reservation) {
        return new ReservationResult(
                reservation.getId(),
                reservation.getUserId(),
                reservation.getTicketTypeId(),
                reservation.getQuantity(),
                reservation.getStatus(),
                reservation.getExpiresAt()
        );
    }
}
