package com.ute.ticket.ticket.domain.entity;

import com.ute.ticket.shared.domain.BaseDomain;
import com.ute.ticket.shared.exception.DomainConflictException;
import com.ute.ticket.shared.exception.DomainValidationException;
import com.ute.ticket.ticket.domain.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Getter
@SuperBuilder
@AllArgsConstructor
public class Ticket extends BaseDomain {

    private final UUID id;
    private final Long orderItemId;
    private final Long sessionId;
    private final String ticketCode;
    private final String qrToken;

    private String attendeeName;
    private String attendeeEmail;

    private TicketStatus status;
    private Instant issuedAt;

    public static Ticket issue(
            UUID id,
            Long orderItemId,
            Long sessionId,
            String ticketCode,
            String qrToken,
            String attendeeName,
            String attendeeEmail
    ) {
        validateId(id);
        validateOrderItemId(orderItemId);
        validateSessionId(sessionId);
        validateTicketCode(ticketCode);
        validateQrToken(qrToken);
        validateAttendee(attendeeName, attendeeEmail);

        Instant now = Instant.now();

        return Ticket.builder()
                .id(id)
                .orderItemId(orderItemId)
                .sessionId(sessionId)
                .ticketCode(ticketCode.trim())
                .qrToken(qrToken.trim())
                .attendeeName(normalize(attendeeName))
                .attendeeEmail(normalize(attendeeEmail))
                .status(TicketStatus.VALID)
                .issuedAt(now)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public boolean isValid() {
        return status == TicketStatus.VALID;
    }

    public boolean isCheckedIn() {
        return status == TicketStatus.CHECKED_IN;
    }

    public boolean isExpired() {
        return status == TicketStatus.EXPIRED;
    }

    public boolean isCancelled() {
        return status == TicketStatus.CANCELLED;
    }

    public boolean isRefunded() {
        return status == TicketStatus.REFUNDED;
    }

    public boolean isVoided() {
        return status == TicketStatus.VOIDED;
    }

    public boolean canRefund() {
        return status == TicketStatus.VALID
                || status == TicketStatus.CANCELLED;
    }

    public void checkIn() {
        ensureValid();

        status = TicketStatus.CHECKED_IN;
    }

    public void assignAttendee(
            String attendeeName,
            String attendeeEmail
    ) {
        ensureValid();

        validateAttendee(attendeeName, attendeeEmail);

        this.attendeeName = normalize(attendeeName);
        this.attendeeEmail = normalize(attendeeEmail);
    }

    public void clearAttendee() {
        ensureValid();

        this.attendeeName = null;
        this.attendeeEmail = null;
    }

    public void expire() {
        ensureValid();

        status = TicketStatus.EXPIRED;
    }

    public void cancel() {
        ensureValid();

        status = TicketStatus.CANCELLED;
    }

    public void refund() {
        if (!canRefund()) {
            throw new DomainConflictException(
                    "Ticket cannot be refunded in its current status "
                            + status + "."
            );
        }

        status = TicketStatus.REFUNDED;
    }

    public void voidTicket() {
        if (status == TicketStatus.REFUNDED) {
            throw new DomainConflictException(
                    "Refunded tickets cannot be voided."
            );
        }

        if (status == TicketStatus.VOIDED) {
            throw new DomainConflictException(
                    "Ticket is already voided."
            );
        }

        if (status == TicketStatus.CHECKED_IN) {
            throw new DomainConflictException(
                    "Checked-in tickets cannot be voided."
            );
        }

        status = TicketStatus.VOIDED;
    }

    public void ensureBelongsToOrderItem(Long orderItemId) {
        if (orderItemId == null
                || !orderItemId.equals(this.orderItemId)) {

            throw new DomainConflictException(
                    "Ticket does not belong to the specified order item."
            );
        }
    }

    public void ensureBelongsToSession(Long sessionId) {
        if (sessionId == null
                || !sessionId.equals(this.sessionId)) {

            throw new DomainConflictException(
                    "Ticket does not belong to the specified session."
            );
        }
    }

    private void ensureValid() {
        if (status != TicketStatus.VALID) {
            throw new DomainConflictException(
                    "Ticket must be valid for this operation, "
                            + "current status is " + status + "."
            );
        }
    }

    private static void validateId(UUID id) {
        if (id == null) {
            throw new DomainValidationException(
                    "Ticket id cannot be null."
            );
        }
    }

    private static void validateOrderItemId(Long orderItemId) {
        if (orderItemId == null) {
            throw new DomainValidationException(
                    "Ticket order item id cannot be null."
            );
        }
    }

    private static void validateSessionId(Long sessionId) {
        if (sessionId == null) {
            throw new DomainValidationException(
                    "Ticket session id cannot be null."
            );
        }
    }

    private static void validateTicketCode(String ticketCode) {
        if (ticketCode == null || ticketCode.isBlank()) {
            throw new DomainValidationException(
                    "Ticket code cannot be blank."
            );
        }
    }

    private static void validateQrToken(String qrToken) {
        if (qrToken == null || qrToken.isBlank()) {
            throw new DomainValidationException(
                    "Ticket QR token cannot be blank."
            );
        }
    }

    private static void validateAttendee(
            String attendeeName,
            String attendeeEmail
    ) {
        if (attendeeName != null && attendeeName.isBlank()) {
            throw new DomainValidationException(
                    "Attendee name cannot be blank."
            );
        }

        if (attendeeEmail != null && attendeeEmail.isBlank()) {
            throw new DomainValidationException(
                    "Attendee email cannot be blank."
            );
        }
    }

    private static String normalize(String value) {
        return value == null ? null : value.trim();
    }
}
