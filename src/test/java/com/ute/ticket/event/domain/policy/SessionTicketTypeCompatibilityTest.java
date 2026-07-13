package com.ute.ticket.event.domain.policy;

import com.ute.ticket.event.domain.enums.SessionStatus;
import com.ute.ticket.event.domain.enums.TicketTypeStatus;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SessionTicketTypeCompatibilityTest {

    private final SessionTicketTypeCompatibility spec = new SessionTicketTypeCompatibility();

    private static final Map<SessionStatus, Set<TicketTypeStatus>> ALLOWED = Map.of(
            SessionStatus.SCHEDULED,
            EnumSet.of(TicketTypeStatus.ACTIVE, TicketTypeStatus.DISABLED, TicketTypeStatus.ARCHIVED, TicketTypeStatus.DELETED),
            SessionStatus.PUBLISHED,
            EnumSet.of(TicketTypeStatus.ACTIVE, TicketTypeStatus.DISABLED, TicketTypeStatus.ARCHIVED, TicketTypeStatus.DELETED),
            SessionStatus.HIDDEN,
            EnumSet.of(TicketTypeStatus.ACTIVE, TicketTypeStatus.DISABLED, TicketTypeStatus.ARCHIVED, TicketTypeStatus.DELETED),
            SessionStatus.POSTPONED,
            EnumSet.of(TicketTypeStatus.ACTIVE, TicketTypeStatus.DISABLED, TicketTypeStatus.ARCHIVED, TicketTypeStatus.DELETED),
            SessionStatus.LIVE,
            EnumSet.of(TicketTypeStatus.ACTIVE, TicketTypeStatus.DISABLED, TicketTypeStatus.ARCHIVED, TicketTypeStatus.DELETED),
            SessionStatus.ENDED,
            EnumSet.of(TicketTypeStatus.DISABLED, TicketTypeStatus.ARCHIVED, TicketTypeStatus.DELETED),
            SessionStatus.CANCELLED,
            EnumSet.of(TicketTypeStatus.DISABLED, TicketTypeStatus.ARCHIVED, TicketTypeStatus.DELETED),
            SessionStatus.DELETED,
            EnumSet.of(TicketTypeStatus.ARCHIVED, TicketTypeStatus.DELETED)
    );

    @Test
    void isSatisfiedByMatchesMatrix2ForAllStatusPairs() {
        for (SessionStatus parent : SessionStatus.values()) {
            for (TicketTypeStatus child : TicketTypeStatus.values()) {
                boolean expected = ALLOWED.get(parent).contains(child);
                if (expected) {
                    assertTrue(spec.isSatisfiedBy(parent, child),
                            () -> "Expected " + child + " to be allowed under " + parent);
                } else {
                    assertFalse(spec.isSatisfiedBy(parent, child),
                            () -> "Expected " + child + " to be rejected under " + parent);
                }
            }
        }
    }

    @Test
    void nullStatusesAreRejected() {
        assertFalse(spec.isSatisfiedBy(null, TicketTypeStatus.ACTIVE));
        assertFalse(spec.isSatisfiedBy(SessionStatus.PUBLISHED, null));
        assertFalse(spec.isSatisfiedBy(null, null));
    }

    @Test
    void violationReasonIsHumanReadable() {
        assertTrue(spec.violationReason(SessionStatus.ENDED, TicketTypeStatus.ACTIVE)
                .contains("TicketType status ACTIVE"));
    }
}
