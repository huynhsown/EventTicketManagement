package com.ute.ticket.event.domain.policy;

import com.ute.ticket.event.domain.enums.EventStatus;
import com.ute.ticket.event.domain.enums.SessionStatus;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class EventSessionCompatibility implements LifecycleSpecification<EventStatus, SessionStatus> {

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
            EnumSet.of(
                    SessionStatus.ENDED,
                    SessionStatus.CANCELLED,
                    SessionStatus.DELETED
            ),
            EventStatus.CANCELLED,
            EnumSet.of(
                    SessionStatus.ENDED,
                    SessionStatus.CANCELLED,
                    SessionStatus.DELETED
            ),
            EventStatus.ARCHIVED,
            EnumSet.of(
                    SessionStatus.ENDED,
                    SessionStatus.CANCELLED,
                    SessionStatus.DELETED
            )
    );

    @Override
    public boolean isSatisfiedBy(EventStatus parent, SessionStatus child) {
        return parent != null
                && child != null
                && ALLOWED.getOrDefault(parent, Set.of()).contains(child);
    }

    @Override
    public String violationReason(EventStatus parent, SessionStatus child) {
        return "Session status " + child + " is not valid while Event is " + parent;
    }
}
