package com.ute.ticket.event.domain.policy;

import java.util.Optional;

public final class PolicyResult {

    private static final PolicyResult APPROVED = new PolicyResult(true, null);

    private final boolean approved;
    private final String reason;

    private PolicyResult(boolean approved, String reason) {
        this.approved = approved;
        this.reason = reason;
    }

    public static PolicyResult approved() {
        return APPROVED;
    }

    public static PolicyResult rejected(String reason) {
        return new PolicyResult(false, reason);
    }

    public boolean isApproved() {
        return approved;
    }

    public boolean isRejected() {
        return !approved;
    }

    public Optional<String> reason() {
        return Optional.ofNullable(reason);
    }
}
