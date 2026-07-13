package com.ute.ticket.event.application.result;

import java.util.List;

public record EventReadinessResult(
        Long eventId,
        boolean ready,
        List<ReadinessCheck> checks
) {

    public record ReadinessCheck(
            String name,
            boolean passed,
            String message
    ) {
    }

    public static EventReadinessResult of(Long eventId, List<ReadinessCheck> checks) {
        boolean ready = checks.stream().allMatch(ReadinessCheck::passed);
        return new EventReadinessResult(eventId, ready, checks);
    }
}
