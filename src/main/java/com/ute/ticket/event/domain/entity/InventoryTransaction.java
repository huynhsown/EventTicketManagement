package com.ute.ticket.event.domain.entity;

import com.ute.ticket.event.domain.enums.InventoryTransactionType;
import com.ute.ticket.shared.domain.BaseDomain;
import com.ute.ticket.shared.exception.DomainValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

@Getter
@SuperBuilder
@AllArgsConstructor
public class InventoryTransaction extends BaseDomain {

    private static final Set<InventoryTransactionType> ZERO_QUANTITY_ALLOWED =
            EnumSet.of(InventoryTransactionType.INITIAL, InventoryTransactionType.RESET);

    private final Long id;
    private final Long ticketTypeId;
    private final InventoryTransactionType type;
    private final Integer quantity;
    private final UUID referenceId;
    private final String note;

    public static InventoryTransaction create(
            Long ticketTypeId,
            InventoryTransactionType type,
            Integer quantity,
            UUID referenceId,
            String note
    ) {
        if (ticketTypeId == null) {
            throw new DomainValidationException("Inventory transaction ticketTypeId cannot be null.");
        }

        if (type == null) {
            throw new DomainValidationException("Inventory transaction type cannot be null.");
        }

        if (quantity == null) {
            throw new DomainValidationException("Inventory transaction quantity cannot be null.");
        }

        if (quantity == 0 && !ZERO_QUANTITY_ALLOWED.contains(type)) {
            throw new DomainValidationException("Inventory transaction quantity must be non-zero.");
        }

        return InventoryTransaction.builder()
                .ticketTypeId(ticketTypeId)
                .type(type)
                .quantity(quantity)
                .referenceId(referenceId)
                .note(note)
                .build();
    }

    public boolean isZeroQuantityAllowed() {
        return ZERO_QUANTITY_ALLOWED.contains(type);
    }
}
