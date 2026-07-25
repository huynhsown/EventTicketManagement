package com.ute.ticket.reservation.domain.entity;

import com.ute.ticket.reservation.domain.enums.ReservationStatus;
import com.ute.ticket.shared.domain.BaseDomain;
import com.ute.ticket.shared.exception.DomainConflictException;
import com.ute.ticket.shared.exception.DomainValidationException;
import com.ute.ticket.shared.exception.ForbiddenException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@SuperBuilder
@AllArgsConstructor
public class Reservation extends BaseDomain {

    private final UUID id;
    private final Long userId;
    private final Long ticketTypeId;
    private Integer quantity;
    private ReservationStatus status;
    private Instant expiresAt;

    public static Reservation create(
            UUID id,
            Long userId,
            Long ticketTypeId,
            Integer quantity,
            Instant expiresAt
    ) {
        if (id == null) {
            throw new DomainValidationException("Reservation id cannot be null.");
        }

        if (userId == null) {
            throw new DomainValidationException("User id cannot be null.");
        }

        if (ticketTypeId == null) {
            throw new DomainValidationException("Ticket type id cannot be null.");
        }

        if (quantity == null || quantity <= 0) {
            throw new DomainValidationException("Reservation quantity must be greater than zero.");
        }

        if (expiresAt == null) {
            throw new DomainValidationException("Reservation expiration cannot be null.");
        }

        Instant now = Instant.now();

        if (!expiresAt.isAfter(now)) {
            throw new DomainValidationException("Reservation expiration must be in the future.");
        }

        return Reservation.builder()
                .id(id)
                .userId(userId)
                .ticketTypeId(ticketTypeId)
                .quantity(quantity)
                .status(ReservationStatus.PENDING)
                .expiresAt(expiresAt)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public boolean isPending() {
        return status == ReservationStatus.PENDING;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public void ensureOwnedBy(Long userId) {
        if (!this.userId.equals(userId)) {
            throw new ForbiddenException("Reservation does not belong to the user");
        }
    }

    public boolean isConfirmed() {
        return status == ReservationStatus.CONFIRMED;
    }

    public boolean isCancelled() {
        return status == ReservationStatus.CANCELLED;
    }

    public boolean isActive() {
        return isPending() && !isExpired();
    }

    public void confirm() {
        ensureUsable();

        status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        ensurePending();

        status = ReservationStatus.CANCELLED;
    }

    public void expire() {
        ensurePending();

        status = ReservationStatus.EXPIRED;
    }

    public void changeQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new DomainValidationException("Reservation quantity must be greater than zero.");
        }

        ensureUsable();

        this.quantity = quantity;
    }

    public void extend(Duration duration) {
        if (duration == null || duration.isNegative() || duration.isZero()) {
            throw new DomainValidationException("Extension duration must be greater than zero.");
        }

        ensureUsable();

        expiresAt = expiresAt.plus(duration);
    }

    private void ensurePending() {
        if (status != ReservationStatus.PENDING) {
            throw new DomainConflictException(
                    "Reservation is already " + status + "."
            );
        }
    }

    private void ensureUsable() {
        ensurePending();

        if (isExpired()) {
            throw new DomainConflictException("Reservation has expired.");
        }
    }
}