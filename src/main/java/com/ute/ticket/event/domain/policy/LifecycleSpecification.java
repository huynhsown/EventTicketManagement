package com.ute.ticket.event.domain.policy;

public interface LifecycleSpecification<PARENT_STATUS, CHILD_STATUS> {

    boolean isSatisfiedBy(PARENT_STATUS parentStatus, CHILD_STATUS childStatus);

    String violationReason(PARENT_STATUS parentStatus, CHILD_STATUS childStatus);
}
