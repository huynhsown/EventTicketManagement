package com.ute.ticket.event.domain.policy;

import com.ute.ticket.event.domain.entity.Event;
import com.ute.ticket.event.domain.entity.Inventory;
import com.ute.ticket.event.domain.entity.Session;
import com.ute.ticket.event.domain.entity.TicketType;
import com.ute.ticket.event.domain.enums.EventStatus;
import com.ute.ticket.event.domain.enums.InventoryStatus;
import com.ute.ticket.event.domain.enums.SessionStatus;
import com.ute.ticket.event.domain.enums.TicketTypeStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EventLifecyclePolicyTest {

    private static final Instant NOW = Instant.parse("2026-01-15T10:00:00Z");
    private static final Instant SALES_START = Instant.parse("2026-01-15T08:00:00Z");
    private static final Instant SALES_END = Instant.parse("2026-01-15T12:00:00Z");

    private final EventLifecyclePolicy policy = new EventLifecyclePolicy();

    @Test
    void approvesWhenWholeChainIsConsistent() {
        PolicyResult result = policy.evaluate(
                event(EventStatus.PUBLISHED),
                session(SessionStatus.PUBLISHED),
                ticketType(TicketTypeStatus.ACTIVE, 4),
                inventory(InventoryStatus.ACTIVE, 100, 0, 0),
                2,
                NOW
        );

        assertTrue(result.isApproved());
    }

    @Test
    void rejectsWhenEventIsNotActive() {
        PolicyResult result = policy.evaluate(
                event(EventStatus.DRAFT),
                session(SessionStatus.SCHEDULED),
                ticketType(TicketTypeStatus.ACTIVE, 4),
                inventory(InventoryStatus.ACTIVE, 100, 0, 0),
                2,
                NOW
        );

        assertRejected(result, "Event is not active");
    }

    @Test
    void rejectsWhenSessionIsLiveUnderPublishedEvent() {
        PolicyResult result = policy.evaluate(
                event(EventStatus.PUBLISHED),
                session(SessionStatus.LIVE),
                ticketType(TicketTypeStatus.ACTIVE, 4),
                inventory(InventoryStatus.ACTIVE, 100, 0, 0),
                2,
                NOW
        );

        assertRejected(result, "Session status LIVE is not valid while Event is PUBLISHED");
    }

    @Test
    void rejectsWhenSessionIsNotOnSale() {
        PolicyResult result = policy.evaluate(
                event(EventStatus.PUBLISHED),
                session(SessionStatus.SCHEDULED),
                ticketType(TicketTypeStatus.ACTIVE, 4),
                inventory(InventoryStatus.ACTIVE, 100, 0, 0),
                2,
                NOW
        );

        assertRejected(result, "Session is not on sale");
    }

    @Test
    void rejectsWhenOutsideSalesWindow() {
        PolicyResult result = policy.evaluate(
                event(EventStatus.PUBLISHED),
                session(SessionStatus.PUBLISHED),
                ticketType(TicketTypeStatus.ACTIVE, 4),
                inventory(InventoryStatus.ACTIVE, 100, 0, 0),
                2,
                Instant.parse("2026-01-15T14:00:00Z")
        );

        assertRejected(result, "sales window");
    }

    @Test
    void sessionTicketTypeLinkIsCheckedOneDirectLevelAtATime() {
        assertTrue(policy.sessionTicketTypeCompatibility().isSatisfiedBy(
                SessionStatus.PUBLISHED, TicketTypeStatus.ACTIVE));
        assertTrue(policy.isTicketTypeAllowedUnderSession(SessionStatus.ENDED, TicketTypeStatus.DISABLED));
        assertTrue(policy.isTicketTypeAllowedUnderSession(SessionStatus.DELETED, TicketTypeStatus.ARCHIVED));
        assertTrue(policy.isTicketTypeAllowedUnderSession(SessionStatus.DELETED, TicketTypeStatus.DELETED));
        assertTrue(!policy.isTicketTypeAllowedUnderSession(SessionStatus.ENDED, TicketTypeStatus.ACTIVE));
        assertTrue(!policy.isTicketTypeAllowedUnderSession(SessionStatus.DELETED, TicketTypeStatus.ACTIVE));
    }

    @Test
    void rejectsWhenTicketTypeIsNotOnSale() {
        PolicyResult result = policy.evaluate(
                event(EventStatus.PUBLISHED),
                session(SessionStatus.PUBLISHED),
                ticketType(TicketTypeStatus.DISABLED, 4),
                inventory(InventoryStatus.ACTIVE, 100, 0, 0),
                2,
                NOW
        );

        assertRejected(result, "TicketType is not on sale");
    }

    @Test
    void rejectsWhenQuantityExceedsPerUserLimit() {
        PolicyResult result = policy.evaluate(
                event(EventStatus.PUBLISHED),
                session(SessionStatus.PUBLISHED),
                ticketType(TicketTypeStatus.ACTIVE, 4),
                inventory(InventoryStatus.ACTIVE, 100, 0, 0),
                5,
                NOW
        );

        assertRejected(result, "per-user limit");
    }

    @Test
    void rejectsWhenInventoryIsDeletedUnderActiveTicketType() {
        PolicyResult result = policy.evaluate(
                event(EventStatus.PUBLISHED),
                session(SessionStatus.PUBLISHED),
                ticketType(TicketTypeStatus.ACTIVE, 4),
                inventory(InventoryStatus.DELETED, 100, 0, 0),
                2,
                NOW
        );

        assertRejected(result, "Inventory status DELETED is not valid while TicketType is ACTIVE");
    }

    @Test
    void rejectsWhenInventoryIsLocked() {
        PolicyResult result = policy.evaluate(
                event(EventStatus.PUBLISHED),
                session(SessionStatus.PUBLISHED),
                ticketType(TicketTypeStatus.ACTIVE, 4),
                inventory(InventoryStatus.LOCKED, 100, 0, 0),
                2,
                NOW
        );

        assertRejected(result, "Insufficient or locked inventory");
    }

    @Test
    void rejectsWhenInventoryStockIsInsufficient() {
        PolicyResult result = policy.evaluate(
                event(EventStatus.PUBLISHED),
                session(SessionStatus.PUBLISHED),
                ticketType(TicketTypeStatus.ACTIVE, 4),
                inventory(InventoryStatus.ACTIVE, 100, 99, 0),
                2,
                NOW
        );

        assertRejected(result, "Insufficient or locked inventory");
    }

    @Test
    void rejectsWhenQuantityIsNotPositive() {
        PolicyResult result = policy.evaluate(
                event(EventStatus.PUBLISHED),
                session(SessionStatus.PUBLISHED),
                ticketType(TicketTypeStatus.ACTIVE, 4),
                inventory(InventoryStatus.ACTIVE, 100, 0, 0),
                0,
                NOW
        );

        assertRejected(result, "greater than zero");
    }

    @Test
    void rejectsWhenAnyAggregateIsMissing() {
        PolicyResult result = policy.evaluate(
                event(EventStatus.PUBLISHED),
                null,
                ticketType(TicketTypeStatus.ACTIVE, 4),
                inventory(InventoryStatus.ACTIVE, 100, 0, 0),
                2,
                NOW
        );

        assertRejected(result, "required");
    }

    private static Event event(EventStatus status) {
        return Event.builder().status(status).build();
    }

    private static Session session(SessionStatus status) {
        return Session.builder()
                .status(status)
                .salesStartAt(SALES_START)
                .salesEndAt(SALES_END)
                .build();
    }

    private static TicketType ticketType(TicketTypeStatus status, int maxPerUser) {
        return TicketType.builder()
                .status(status)
                .maxPerUser(maxPerUser)
                .build();
    }

    private static Inventory inventory(InventoryStatus status, int totalStock, int reservedStock, int soldStock) {
        return Inventory.builder()
                .status(status)
                .totalStock(totalStock)
                .reservedStock(reservedStock)
                .soldStock(soldStock)
                .build();
    }

    private static void assertRejected(PolicyResult result, String reasonPart) {
        assertTrue(result.isRejected());
        assertTrue(result.reason().orElse("").contains(reasonPart),
                () -> "Expected rejection reason to contain '" + reasonPart + "' but was: " + result.reason());
    }
}
