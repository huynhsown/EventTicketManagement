package com.ute.ticket.event.domain.entity;

import com.ute.ticket.event.domain.enums.SessionStatus;
import com.ute.ticket.shared.domain.BaseDomain;
import com.ute.ticket.shared.exception.DomainConflictException;
import com.ute.ticket.shared.exception.DomainValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

@Getter
@SuperBuilder
@AllArgsConstructor
public class Session extends BaseDomain {

    private static final Set<SessionStatus> TERMINAL_STATUSES =
            EnumSet.of(SessionStatus.CANCELLED, SessionStatus.DELETED);

    private static final Duration MIN_DURATION = Duration.ofMinutes(15);
    private static final Duration MAX_DURATION = Duration.ofHours(48);

    private final Long id;
    private final Long eventId;
    private Instant startTime;
    private Instant endTime;
    private Instant salesStartAt;
    private Instant salesEndAt;
    private SessionStatus status;

    public static Session create(
            Long eventId,
            Instant startTime,
            Instant endTime,
            Instant salesStartAt,
            Instant salesEndAt
    ) {
        if (eventId == null) {
            throw new DomainValidationException("Session event id cannot be null.");
        }

        validateTimeRange(startTime, endTime);
        validateSalesWindow(salesStartAt, salesEndAt, startTime);

        if (startTime.isBefore(Instant.now())) {
            throw new DomainValidationException("Session start time must be in the future.");
        }

        return Session.builder()
                .eventId(eventId)
                .startTime(startTime)
                .endTime(endTime)
                .salesStartAt(salesStartAt)
                .salesEndAt(salesEndAt)
                .status(SessionStatus.SCHEDULED)
                .build();
    }

    public void reschedule(Instant startTime, Instant endTime) {
        if (status == SessionStatus.LIVE) {
            throw new DomainConflictException("Live sessions cannot be rescheduled.");
        }

        if (status == SessionStatus.CANCELLED) {
            throw new DomainConflictException("Cancelled sessions cannot be rescheduled.");
        }

        if (status == SessionStatus.DELETED) {
            throw new DomainConflictException("Deleted sessions cannot be rescheduled.");
        }

        validateTimeRange(startTime, endTime);
        validateSalesWindow(salesStartAt, salesEndAt, startTime);

        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void changeSalesPeriod(Instant salesStartAt, Instant salesEndAt) {
        if (status == SessionStatus.LIVE || status == SessionStatus.ENDED) {
            throw new DomainConflictException("Sales window cannot be changed once the session has started.");
        }

        if (status == SessionStatus.CANCELLED || status == SessionStatus.DELETED) {
            throw new DomainConflictException("Sales window cannot be changed on a terminal session.");
        }

        validateSalesWindow(salesStartAt, salesEndAt, startTime);

        this.salesStartAt = salesStartAt;
        this.salesEndAt = salesEndAt;
    }

    public void postpone() {
        if (status != SessionStatus.SCHEDULED && status != SessionStatus.PUBLISHED) {
            throw new DomainConflictException("Only scheduled or published sessions can be postponed.");
        }

        status = SessionStatus.POSTPONED;
    }

    public void reopen() {
        if (status == SessionStatus.POSTPONED) {
            status = SessionStatus.SCHEDULED;
            return;
        }

        if (status == SessionStatus.CANCELLED) {
            status = SessionStatus.SCHEDULED;
            return;
        }

        throw new DomainConflictException("Only postponed or cancelled sessions can be reopened.");
    }

    public void publish() {
        if (status == SessionStatus.PUBLISHED) {
            throw new DomainConflictException("Session is already published.");
        }

        if (status == SessionStatus.CANCELLED || status == SessionStatus.DELETED) {
            throw new DomainConflictException("Terminal sessions cannot be published.");
        }

        if (status == SessionStatus.ENDED) {
            throw new DomainConflictException("Ended sessions cannot be published.");
        }

        status = SessionStatus.PUBLISHED;
    }

    public void hide() {
        if (status == SessionStatus.HIDDEN) {
            throw new DomainConflictException("Session is already hidden.");
        }

        if (status != SessionStatus.PUBLISHED && status != SessionStatus.SCHEDULED) {
            throw new DomainConflictException("Session cannot be hidden in its current state.");
        }

        status = SessionStatus.HIDDEN;
    }

    public void show() {
        if (status != SessionStatus.HIDDEN) {
            throw new DomainConflictException("Only hidden sessions can be shown.");
        }

        status = SessionStatus.PUBLISHED;
    }

    public void cancel(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new DomainValidationException("Cancellation reason is mandatory.");
        }

        if (status == SessionStatus.CANCELLED) {
            throw new DomainConflictException("Session is already cancelled.");
        }

        if (status == SessionStatus.DELETED || status == SessionStatus.ENDED) {
            throw new DomainConflictException("Terminal sessions cannot be cancelled.");
        }

        status = SessionStatus.CANCELLED;
    }

    public void markStarted() {
        if (status != SessionStatus.PUBLISHED) {
            throw new DomainConflictException("Only published sessions can start.");
        }

        status = SessionStatus.LIVE;
    }

    public void markEnded() {
        if (status != SessionStatus.LIVE) {
            throw new DomainConflictException("Only live sessions can be ended.");
        }

        status = SessionStatus.ENDED;
    }

    public boolean isOnSale() {
        return status == SessionStatus.PUBLISHED || status == SessionStatus.LIVE;
    }

    public boolean isTerminal() {
        return TERMINAL_STATUSES.contains(status);
    }

    public boolean overlaps(Session other) {
        return startTime.isBefore(other.endTime) && other.startTime.isBefore(endTime);
    }

    private static void validateTimeRange(Instant startTime, Instant endTime) {
        if (startTime == null || endTime == null) {
            throw new DomainValidationException("Session start and end times cannot be null.");
        }

        if (!startTime.isBefore(endTime)) {
            throw new DomainValidationException("Session start time must be before end time.");
        }

        Duration duration = Duration.between(startTime, endTime);
        if (duration.compareTo(MIN_DURATION) < 0) {
            throw new DomainValidationException("Session duration is below the minimum of " + MIN_DURATION.toMinutes() + " minutes.");
        }

        if (duration.compareTo(MAX_DURATION) > 0) {
            throw new DomainValidationException("Session duration exceeds the maximum of " + MAX_DURATION.toHours() + " hours.");
        }
    }

    private static void validateSalesWindow(Instant salesStartAt, Instant salesEndAt, Instant startTime) {
        if (salesStartAt == null || salesEndAt == null) {
            throw new DomainValidationException("Session sales window cannot be null.");
        }

        if (!salesStartAt.isBefore(salesEndAt)) {
            throw new DomainValidationException("Session sales start must be before sales end.");
        }

        if (startTime != null && salesEndAt.isAfter(startTime)) {
            throw new DomainValidationException("Session sales must close before or at show start.");
        }
    }
}
