package com.ute.ticket.order.domain.entity;

import com.ute.ticket.order.domain.enums.OrderStatus;
import com.ute.ticket.shared.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@SuperBuilder
@AllArgsConstructor
public class Order extends BaseDomain {

    private final Long id;
    private final String code;
    private final UUID reservationId;
    private final Long userId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private final List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Order item cannot be null");
        }
        items.add(item);
        recalculateTotalAmount();
    }

    public void addItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException(
                    "Order items cannot be empty"
            );
        }
        this.items.addAll(items);
        recalculateTotalAmount();
    }

    public void markAsPaid() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending order can be paid"
            );
        }
        this.status = OrderStatus.PAID;
    }

    public void confirm() {
        if (status != OrderStatus.PAID) {
            throw new IllegalStateException(
                    "Only paid order can be confirmed"
            );
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void complete() {
        if (status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Only confirmed order can be completed"
            );
        }
        this.status = OrderStatus.COMPLETED;
    }

    public void cancel() {
        if (
                status != OrderStatus.PENDING &&
                        status != OrderStatus.PAID
        ) {
            throw new IllegalStateException(
                    "Order cannot be cancelled"
            );
        }
        this.status = OrderStatus.CANCELLED;
    }

    public void fail() {
        if (
                status == OrderStatus.COMPLETED ||
                        status == OrderStatus.REFUNDED
        ) {
            throw new IllegalStateException(
                    "Completed/refunded order cannot fail"
            );
        }
        this.status = OrderStatus.FAILED;
    }

    public void refund() {
        if (
                status != OrderStatus.PAID &&
                        status != OrderStatus.CONFIRMED &&
                        status != OrderStatus.COMPLETED
        ) {
            throw new IllegalStateException(
                    "Only paid orders can be refunded"
            );
        }
        this.status = OrderStatus.REFUNDED;
    }

    public boolean canCancel() {
        return status == OrderStatus.PENDING
                || status == OrderStatus.PAID;
    }

    public boolean canRefund() {
        return status == OrderStatus.PAID
                || status == OrderStatus.CONFIRMED
                || status == OrderStatus.COMPLETED;
    }

    private void recalculateTotalAmount() {
        this.totalAmount =
                items.stream()
                        .map(OrderItem::getSubtotal)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );
    }
}
