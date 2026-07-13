package com.ute.ticket.event.domain.policy;

import com.ute.ticket.event.domain.enums.InventoryStatus;
import com.ute.ticket.event.domain.enums.TicketTypeStatus;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class TicketTypeInventoryCompatibility implements LifecycleSpecification<TicketTypeStatus, InventoryStatus> {

    private static final Map<TicketTypeStatus, Set<InventoryStatus>> ALLOWED = Map.of(
            TicketTypeStatus.ACTIVE,
            EnumSet.of(
                    InventoryStatus.ACTIVE,
                    InventoryStatus.LOCKED
            ),
            TicketTypeStatus.DISABLED,
            EnumSet.of(
                    InventoryStatus.ACTIVE,
                    InventoryStatus.LOCKED,
                    InventoryStatus.DELETED
            ),
            TicketTypeStatus.ARCHIVED,
            EnumSet.of(
                    InventoryStatus.LOCKED,
                    InventoryStatus.DELETED
            ),
            TicketTypeStatus.DELETED,
            EnumSet.of(
                    InventoryStatus.LOCKED,
                    InventoryStatus.DELETED
            )
    );

    @Override
    public boolean isSatisfiedBy(TicketTypeStatus parent, InventoryStatus child) {
        return parent != null
                && child != null
                && ALLOWED.getOrDefault(parent, Set.of()).contains(child);
    }

    @Override
    public String violationReason(TicketTypeStatus parent, InventoryStatus child) {
        return "Inventory status " + child + " is not valid while TicketType is " + parent;
    }
}
