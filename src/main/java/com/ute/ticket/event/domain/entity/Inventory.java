package com.ute.ticket.event.domain.entity;

import com.ute.ticket.event.domain.enums.InventoryStatus;
import com.ute.ticket.event.domain.enums.InventoryTransactionType;
import com.ute.ticket.shared.domain.BaseDomain;
import com.ute.ticket.shared.exception.DomainConflictException;
import com.ute.ticket.shared.exception.DomainValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
@AllArgsConstructor
public class Inventory extends BaseDomain {

    private final Long ticketTypeId;
    private Integer totalStock;
    private Integer reservedStock;
    private Integer soldStock;
    private InventoryStatus status;

    public static Inventory create(Long ticketTypeId, int initialTotalStock) {
        if (ticketTypeId == null) {
            throw new DomainValidationException("Inventory ticket type id cannot be null.");
        }

        if (initialTotalStock < 0) {
            throw new DomainValidationException("Initial stock cannot be negative.");
        }

        return Inventory.builder()
                .ticketTypeId(ticketTypeId)
                .totalStock(initialTotalStock)
                .reservedStock(0)
                .soldStock(0)
                .status(InventoryStatus.ACTIVE)
                .build();
    }

    public int availableStock() {
        return totalStock - reservedStock - soldStock;
    }

    public boolean isLocked() {
        return status == InventoryStatus.LOCKED;
    }

    public boolean isDepleted() {
        return availableStock() == 0;
    }

    public InventoryTransaction initializationTransaction(String note) {
        return InventoryTransaction.create(ticketTypeId, InventoryTransactionType.INITIAL, totalStock, null, note);
    }

    public InventoryTransaction increase(int quantity, UUID referenceId, String note) {
        ensureActive();

        if (quantity <= 0) {
            throw new DomainValidationException("Increase quantity must be greater than zero.");
        }

        totalStock += quantity;

        return InventoryTransaction.create(ticketTypeId, InventoryTransactionType.INCREASE, quantity, referenceId, note);
    }

    public InventoryTransaction decrease(int quantity, UUID referenceId, String note) {
        ensureActive();

        if (quantity <= 0) {
            throw new DomainValidationException("Decrease quantity must be greater than zero.");
        }

        if (quantity > availableStock()) {
            throw new DomainConflictException("Cannot decrease below available stock.");
        }

        totalStock -= quantity;

        return InventoryTransaction.create(ticketTypeId, InventoryTransactionType.DECREASE, -quantity, referenceId, note);
    }

    public InventoryTransaction adjust(int delta, String reason, UUID referenceId) {
        ensureActive();

        if (delta == 0) {
            throw new DomainValidationException("Adjustment delta cannot be zero.");
        }

        if (reason == null || reason.isBlank()) {
            throw new DomainValidationException("Adjustment reason is mandatory.");
        }

        int newAvailable = availableStock() + delta;
        if (newAvailable < 0) {
            throw new DomainConflictException("Adjustment would drive available stock below zero.");
        }

        totalStock += delta;

        return InventoryTransaction.create(ticketTypeId, InventoryTransactionType.ADJUSTMENT, delta, referenceId, reason);
    }

    public InventoryTransaction correct(int newTotalStock, String reason) {
        ensureActive();

        if (newTotalStock < 0) {
            throw new DomainValidationException("Corrected total stock cannot be negative.");
        }

        if (reason == null || reason.isBlank()) {
            throw new DomainValidationException("Correction reason is mandatory.");
        }

        if (newTotalStock < reservedStock + soldStock) {
            throw new DomainConflictException("Corrected total stock cannot be below reserved and sold stock.");
        }

        int delta = newTotalStock - totalStock;
        totalStock = newTotalStock;

        return InventoryTransaction.create(ticketTypeId, InventoryTransactionType.CORRECTION, delta, null, reason);
    }

    public InventoryTransaction reset(String reason) {
        ensureActive();

        if (reason == null || reason.isBlank()) {
            throw new DomainValidationException("Reset reason is mandatory.");
        }

        totalStock = 0;
        reservedStock = 0;
        soldStock = 0;

        return InventoryTransaction.create(ticketTypeId, InventoryTransactionType.RESET, 0, null, reason);
    }

    public InventoryTransaction sync(int delta, String note) {
        ensureActive();

        if (delta == 0) {
            throw new DomainValidationException("Sync delta cannot be zero.");
        }

        int newAvailable = availableStock() + delta;
        if (newAvailable < 0) {
            throw new DomainConflictException("Sync would drive available stock below zero.");
        }

        totalStock += delta;

        return InventoryTransaction.create(ticketTypeId, InventoryTransactionType.SYNC, delta, null, note);
    }

    public InventoryTransaction importStock(int quantity, UUID referenceId, String note) {
        ensureActive();

        if (quantity <= 0) {
            throw new DomainValidationException("Import quantity must be greater than zero.");
        }

        totalStock += quantity;

        return InventoryTransaction.create(ticketTypeId, InventoryTransactionType.IMPORT, quantity, referenceId, note);
    }

    public InventoryTransaction reserve(int quantity, UUID referenceId) {
        ensureActive();

        if (quantity <= 0) {
            throw new DomainValidationException("Reserve quantity must be greater than zero.");
        }

        if (quantity > availableStock()) {
            throw new DomainConflictException("Not enough available stock to reserve.");
        }

        reservedStock += quantity;

        return InventoryTransaction.create(ticketTypeId, InventoryTransactionType.RESERVE, quantity, referenceId, null);
    }

    public InventoryTransaction release(int quantity, UUID referenceId) {
        ensureActive();

        if (quantity <= 0) {
            throw new DomainValidationException("Release quantity must be greater than zero.");
        }

        if (quantity > reservedStock) {
            throw new DomainConflictException("Cannot release more than the reserved stock.");
        }

        reservedStock -= quantity;

        return InventoryTransaction.create(ticketTypeId, InventoryTransactionType.RELEASE, -quantity, referenceId, null);
    }

    public InventoryTransaction confirm(int quantity, UUID referenceId) {
        ensureActive();

        if (quantity <= 0) {
            throw new DomainValidationException("Confirm quantity must be greater than zero.");
        }

        if (quantity > reservedStock) {
            throw new DomainConflictException("Cannot confirm more than the reserved stock.");
        }

        reservedStock -= quantity;
        soldStock += quantity;

        return InventoryTransaction.create(ticketTypeId, InventoryTransactionType.CONFIRM, quantity, referenceId, null);
    }

    public InventoryTransaction expire(int quantity, UUID referenceId) {
        ensureActive();

        if (quantity <= 0) {
            throw new DomainValidationException("Expire quantity must be greater than zero.");
        }

        if (quantity > reservedStock) {
            throw new DomainConflictException("Cannot expire more than the reserved stock.");
        }

        reservedStock -= quantity;

        return InventoryTransaction.create(ticketTypeId, InventoryTransactionType.EXPIRY, -quantity, referenceId, null);
    }

    public void lock() {
        if (status == InventoryStatus.LOCKED) {
            throw new DomainConflictException("Inventory is already locked.");
        }

        if (status == InventoryStatus.DELETED) {
            throw new DomainConflictException("Deleted inventory cannot be locked.");
        }

        status = InventoryStatus.LOCKED;
    }

    public void unlock() {
        if (status != InventoryStatus.LOCKED) {
            throw new DomainConflictException("Only locked inventory can be unlocked.");
        }

        status = InventoryStatus.ACTIVE;
    }

    private void ensureActive() {
        if (status == InventoryStatus.LOCKED) {
            throw new DomainConflictException("Inventory is locked and cannot be modified.");
        }

        if (status == InventoryStatus.DELETED) {
            throw new DomainConflictException("Deleted inventory cannot be modified.");
        }
    }
}
