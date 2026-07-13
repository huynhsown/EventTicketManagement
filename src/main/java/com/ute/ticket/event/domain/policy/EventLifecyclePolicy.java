package com.ute.ticket.event.domain.policy;

import com.ute.ticket.event.domain.entity.Event;
import com.ute.ticket.event.domain.entity.Inventory;
import com.ute.ticket.event.domain.entity.Session;
import com.ute.ticket.event.domain.entity.TicketType;
import com.ute.ticket.event.domain.enums.EventStatus;
import com.ute.ticket.event.domain.enums.InventoryStatus;
import com.ute.ticket.event.domain.enums.SessionStatus;
import com.ute.ticket.event.domain.enums.TicketTypeStatus;

import java.time.Instant;

public class EventLifecyclePolicy {

    private final EventSessionCompatibility eventSessionCompatibility = new EventSessionCompatibility();
    private final SessionTicketTypeCompatibility sessionTicketTypeCompatibility = new SessionTicketTypeCompatibility();
    private final TicketTypeInventoryCompatibility ticketTypeInventoryCompatibility = new TicketTypeInventoryCompatibility();

    public PolicyResult evaluate(Event event, Session session, TicketType ticketType, Inventory inventory, int qty) {
        return evaluate(event, session, ticketType, inventory, qty, Instant.now());
    }

    public PolicyResult evaluate(
            Event event,
            Session session,
            TicketType ticketType,
            Inventory inventory,
            int qty,
            Instant now
    ) {
        if (event == null || session == null || ticketType == null || inventory == null) {
            return PolicyResult.rejected("Event, session, ticket type and inventory are required.");
        }

        if (qty <= 0) {
            return PolicyResult.rejected("Requested quantity must be greater than zero.");
        }

        if (!event.isActive()) {
            return PolicyResult.rejected("Event is not active: " + event.getStatus());
        }

        if (!eventSessionCompatibility.isSatisfiedBy(event.getStatus(), session.getStatus())) {
            return PolicyResult.rejected(eventSessionCompatibility.violationReason(event.getStatus(), session.getStatus()));
        }

        if (!session.isOnSale()) {
            return PolicyResult.rejected("Session is not on sale: " + session.getStatus());
        }

        if (now.isBefore(session.getSalesStartAt()) || now.isAfter(session.getSalesEndAt())) {
            return PolicyResult.rejected("Purchase is outside the session sales window.");
        }

        if (!sessionTicketTypeCompatibility.isSatisfiedBy(session.getStatus(), ticketType.getStatus())) {
            return PolicyResult.rejected(sessionTicketTypeCompatibility.violationReason(session.getStatus(), ticketType.getStatus()));
        }

        if (!ticketType.isOnSale()) {
            return PolicyResult.rejected("TicketType is not on sale: " + ticketType.getStatus());
        }

        if (qty > ticketType.getMaxPerUser()) {
            return PolicyResult.rejected("Requested quantity exceeds the per-user limit of " + ticketType.getMaxPerUser() + ".");
        }

        if (!ticketTypeInventoryCompatibility.isSatisfiedBy(ticketType.getStatus(), inventory.getStatus())) {
            return PolicyResult.rejected(ticketTypeInventoryCompatibility.violationReason(ticketType.getStatus(), inventory.getStatus()));
        }

        if (inventory.isLocked() || inventory.availableStock() < qty) {
            return PolicyResult.rejected("Insufficient or locked inventory.");
        }

        return PolicyResult.approved();
    }

    public boolean isSessionAllowedUnderEvent(EventStatus parent, SessionStatus child) {
        return eventSessionCompatibility.isSatisfiedBy(parent, child);
    }

    public boolean isTicketTypeAllowedUnderSession(SessionStatus parent, TicketTypeStatus child) {
        return sessionTicketTypeCompatibility.isSatisfiedBy(parent, child);
    }

    public boolean isInventoryAllowedUnderTicketType(TicketTypeStatus parent, InventoryStatus child) {
        return ticketTypeInventoryCompatibility.isSatisfiedBy(parent, child);
    }

    public EventSessionCompatibility eventSessionCompatibility() {
        return eventSessionCompatibility;
    }

    public SessionTicketTypeCompatibility sessionTicketTypeCompatibility() {
        return sessionTicketTypeCompatibility;
    }

    public TicketTypeInventoryCompatibility ticketTypeInventoryCompatibility() {
        return ticketTypeInventoryCompatibility;
    }
}
