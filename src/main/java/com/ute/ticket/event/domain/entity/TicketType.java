package com.ute.ticket.event.domain.entity;

import com.ute.ticket.event.domain.enums.TicketTypeStatus;
import com.ute.ticket.shared.domain.BaseDomain;
import com.ute.ticket.shared.exception.DomainConflictException;
import com.ute.ticket.shared.exception.DomainValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@SuperBuilder
@AllArgsConstructor
public class TicketType extends BaseDomain {

    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 4000;
    private static final BigDecimal MAX_PRICE = new BigDecimal("999999.99");
    private static final int MAX_PER_USER_MAX = 100;

    private final Long id;
    private final Long sessionId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer maxPerUser;
    private TicketTypeStatus status;

    public static TicketType create(
            Long sessionId,
            String name,
            String description,
            BigDecimal price,
            Integer maxPerUser
    ) {
        if (sessionId == null) {
            throw new DomainValidationException("Ticket type session id cannot be null.");
        }

        validateName(name);
        validateDescription(description);
        validatePrice(price);
        validateMaxPerUser(maxPerUser);

        return TicketType.builder()
                .sessionId(sessionId)
                .name(name.trim())
                .description(description)
                .price(price.setScale(2, RoundingMode.UNNECESSARY))
                .maxPerUser(maxPerUser)
                .status(TicketTypeStatus.ACTIVE)
                .build();
    }

    public void rename(String name) {
        ensureNotArchived();
        validateName(name);
        this.name = name.trim();
    }

    public void changeDescription(String description) {
        ensureNotArchived();
        validateDescription(description);
        this.description = description;
    }

    public void changePrice(BigDecimal price) {
        ensureNotArchived();
        validatePrice(price);
        this.price = price.setScale(2, RoundingMode.UNNECESSARY);
    }

    public void changeMaxPerUser(Integer maxPerUser) {
        ensureNotArchived();
        validateMaxPerUser(maxPerUser);
        this.maxPerUser = maxPerUser;
    }

    public void activate() {
        if (status == TicketTypeStatus.ACTIVE) {
            throw new DomainConflictException("Ticket type is already active.");
        }

        if (status == TicketTypeStatus.ARCHIVED) {
            throw new DomainConflictException("Archived ticket type cannot be activated. Restore first.");
        }

        status = TicketTypeStatus.ACTIVE;
    }

    public void disable() {
        if (status == TicketTypeStatus.DISABLED) {
            throw new DomainConflictException("Ticket type is already disabled.");
        }

        if (status == TicketTypeStatus.ARCHIVED) {
            throw new DomainConflictException("Archived ticket type cannot be disabled.");
        }

        status = TicketTypeStatus.DISABLED;
    }

    public void archive() {
        if (status == TicketTypeStatus.ARCHIVED) {
            throw new DomainConflictException("Ticket type is already archived.");
        }

        status = TicketTypeStatus.ARCHIVED;
    }

    public void restore() {
        if (status != TicketTypeStatus.ARCHIVED) {
            throw new DomainConflictException("Only archived ticket types can be restored.");
        }

        status = TicketTypeStatus.ACTIVE;
    }

    public boolean isOnSale() {
        return status == TicketTypeStatus.ACTIVE;
    }

    private void ensureNotArchived() {
        if (status == TicketTypeStatus.ARCHIVED) {
            throw new DomainConflictException("Archived ticket type cannot be modified.");
        }
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainValidationException("Ticket type name cannot be blank.");
        }

        if (name.length() > MAX_NAME_LENGTH) {
            throw new DomainValidationException("Ticket type name must not exceed " + MAX_NAME_LENGTH + " characters.");
        }
    }

    private static void validateDescription(String description) {
        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new DomainValidationException("Ticket type description must not exceed " + MAX_DESCRIPTION_LENGTH + " characters.");
        }
    }

    private static void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new DomainValidationException("Ticket type price cannot be null.");
        }

        if (price.signum() < 0) {
            throw new DomainValidationException("Ticket type price cannot be negative.");
        }

        if (price.scale() > 2) {
            throw new DomainValidationException("Ticket type price must have at most 2 decimal places.");
        }

        if (price.compareTo(MAX_PRICE) > 0) {
            throw new DomainValidationException("Ticket type price cannot exceed " + MAX_PRICE + ".");
        }
    }

    private static void validateMaxPerUser(Integer maxPerUser) {
        if (maxPerUser == null) {
            throw new DomainValidationException("Ticket type maxPerUser cannot be null.");
        }

        if (maxPerUser < 1) {
            throw new DomainValidationException("Ticket type maxPerUser must be at least 1.");
        }

        if (maxPerUser > MAX_PER_USER_MAX) {
            throw new DomainValidationException("Ticket type maxPerUser cannot exceed " + MAX_PER_USER_MAX + ".");
        }
    }
}
