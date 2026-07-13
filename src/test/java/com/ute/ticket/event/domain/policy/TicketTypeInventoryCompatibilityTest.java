package com.ute.ticket.event.domain.policy;

import com.ute.ticket.event.domain.enums.InventoryStatus;
import com.ute.ticket.event.domain.enums.TicketTypeStatus;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TicketTypeInventoryCompatibilityTest {

    private final TicketTypeInventoryCompatibility spec = new TicketTypeInventoryCompatibility();

    private static final Map<TicketTypeStatus, Set<InventoryStatus>> ALLOWED = Map.of(
            TicketTypeStatus.ACTIVE,
            EnumSet.of(InventoryStatus.ACTIVE, InventoryStatus.LOCKED),
            TicketTypeStatus.DISABLED,
            EnumSet.of(InventoryStatus.ACTIVE, InventoryStatus.LOCKED, InventoryStatus.DELETED),
            TicketTypeStatus.ARCHIVED,
            EnumSet.of(InventoryStatus.LOCKED, InventoryStatus.DELETED),
            TicketTypeStatus.DELETED,
            EnumSet.of(InventoryStatus.LOCKED, InventoryStatus.DELETED)
    );

    @Test
    void isSatisfiedByMatchesMatrix3ForAllStatusPairs() {
        for (TicketTypeStatus parent : TicketTypeStatus.values()) {
            for (InventoryStatus child : InventoryStatus.values()) {
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
        assertFalse(spec.isSatisfiedBy(null, InventoryStatus.ACTIVE));
        assertFalse(spec.isSatisfiedBy(TicketTypeStatus.ACTIVE, null));
        assertFalse(spec.isSatisfiedBy(null, null));
    }

    @Test
    void violationReasonIsHumanReadable() {
        assertTrue(spec.violationReason(TicketTypeStatus.ARCHIVED, InventoryStatus.ACTIVE)
                .contains("Inventory status ACTIVE"));
    }
}
