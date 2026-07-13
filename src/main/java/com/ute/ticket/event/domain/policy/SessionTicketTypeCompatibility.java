package com.ute.ticket.event.domain.policy;

import com.ute.ticket.event.domain.enums.SessionStatus;
import com.ute.ticket.event.domain.enums.TicketTypeStatus;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class SessionTicketTypeCompatibility implements LifecycleSpecification<SessionStatus, TicketTypeStatus> {

    private static final Map<SessionStatus, Set<TicketTypeStatus>> ALLOWED = Map.of(
            SessionStatus.SCHEDULED,
            EnumSet.of(
                    TicketTypeStatus.ACTIVE,
                    TicketTypeStatus.DISABLED,
                    TicketTypeStatus.ARCHIVED,
                    TicketTypeStatus.DELETED
            ),
            SessionStatus.PUBLISHED,
            EnumSet.of(
                    TicketTypeStatus.ACTIVE,
                    TicketTypeStatus.DISABLED,
                    TicketTypeStatus.ARCHIVED,
                    TicketTypeStatus.DELETED
            ),
            SessionStatus.HIDDEN,
            EnumSet.of(
                    TicketTypeStatus.ACTIVE,
                    TicketTypeStatus.DISABLED,
                    TicketTypeStatus.ARCHIVED,
                    TicketTypeStatus.DELETED
            ),
            SessionStatus.POSTPONED,
            EnumSet.of(
                    TicketTypeStatus.ACTIVE,
                    TicketTypeStatus.DISABLED,
                    TicketTypeStatus.ARCHIVED,
                    TicketTypeStatus.DELETED
            ),
            SessionStatus.LIVE,
            EnumSet.of(
                    TicketTypeStatus.ACTIVE,
                    TicketTypeStatus.DISABLED,
                    TicketTypeStatus.ARCHIVED,
                    TicketTypeStatus.DELETED
            ),
            SessionStatus.ENDED,
            EnumSet.of(
                    TicketTypeStatus.DISABLED,
                    TicketTypeStatus.ARCHIVED,
                    TicketTypeStatus.DELETED
            ),
            SessionStatus.CANCELLED,
            EnumSet.of(
                    TicketTypeStatus.DISABLED,
                    TicketTypeStatus.ARCHIVED,
                    TicketTypeStatus.DELETED
            ),
            SessionStatus.DELETED,
            EnumSet.of(
                    TicketTypeStatus.ARCHIVED,
                    TicketTypeStatus.DELETED
            )
    );

    @Override
    public boolean isSatisfiedBy(SessionStatus parent, TicketTypeStatus child) {
        return parent != null
                && child != null
                && ALLOWED.getOrDefault(parent, Set.of()).contains(child);
    }

    @Override
    public String violationReason(SessionStatus parent, TicketTypeStatus child) {
        return "TicketType status " + child + " is not valid while Session is " + parent;
    }
}
