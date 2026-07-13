package com.ute.ticket.event.domain.policy;

import com.ute.ticket.event.domain.enums.EventStatus;
import com.ute.ticket.event.domain.enums.SessionStatus;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventSessionCompatibilityTest {

    private final EventSessionCompatibility spec = new EventSessionCompatibility();

    private static final Map<EventStatus, Set<SessionStatus>> ALLOWED = Map.of(
            EventStatus.DRAFT,
            EnumSet.of(
                    SessionStatus.SCHEDULED,
                    SessionStatus.POSTPONED,
                    SessionStatus.HIDDEN,
                    SessionStatus.CANCELLED,
                    SessionStatus.DELETED
            ),
            EventStatus.PUBLISHED,
            EnumSet.of(
                    SessionStatus.SCHEDULED,
                    SessionStatus.PUBLISHED,
                    SessionStatus.HIDDEN,
                    SessionStatus.POSTPONED,
                    SessionStatus.CANCELLED,
                    SessionStatus.DELETED
            ),
            EventStatus.SALES_PAUSED,
            EnumSet.allOf(SessionStatus.class),
            EventStatus.LIVE,
            EnumSet.allOf(SessionStatus.class),
            EventStatus.ENDED,
            EnumSet.of(SessionStatus.ENDED, SessionStatus.CANCELLED, SessionStatus.DELETED),
            EventStatus.CANCELLED,
            EnumSet.of(SessionStatus.ENDED, SessionStatus.CANCELLED, SessionStatus.DELETED),
            EventStatus.ARCHIVED,
            EnumSet.of(SessionStatus.ENDED, SessionStatus.CANCELLED, SessionStatus.DELETED)
    );

    @Test
    void isSatisfiedByMatchesMatrix1ForAllStatusPairs() {
        for (EventStatus parent : EventStatus.values()) {
            for (SessionStatus child : SessionStatus.values()) {
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
        assertFalse(spec.isSatisfiedBy(null, SessionStatus.SCHEDULED));
        assertFalse(spec.isSatisfiedBy(EventStatus.PUBLISHED, null));
        assertFalse(spec.isSatisfiedBy(null, null));
    }

    @Test
    void violationReasonIsHumanReadable() {
        assertTrue(spec.violationReason(EventStatus.ENDED, SessionStatus.PUBLISHED)
                .contains("Session status PUBLISHED"));
    }
}
