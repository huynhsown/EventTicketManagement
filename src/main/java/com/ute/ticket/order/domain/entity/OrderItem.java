package com.ute.ticket.order.domain.entity;

import com.ute.ticket.shared.domain.BaseDomain;
import com.ute.ticket.shared.exception.DomainValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@SuperBuilder
@AllArgsConstructor
public class OrderItem extends BaseDomain {

    private final Long id;
    private final Long orderId;
    private final Long ticketTypeId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public static OrderItem create(
            Long orderId,
            Long ticketTypeId,
            int quantity,
            BigDecimal unitPrice
    ) {
        if (ticketTypeId == null) {
            throw new DomainValidationException(
                    "Ticket type id cannot be null."
            );
        }

        if (quantity <= 0) {
            throw new DomainValidationException(
                    "Quantity must be greater than zero."
            );
        }

        if (unitPrice == null) {
            throw new DomainValidationException(
                    "Unit price cannot be null."
            );
        }

        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainValidationException(
                    "Unit price cannot be negative."
            );
        }

        return OrderItem.builder()
                .orderId(orderId)
                .ticketTypeId(ticketTypeId)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .subtotal(
                        unitPrice.multiply(
                                BigDecimal.valueOf(quantity)
                        )
                )
                .build();
    }

    public void updateQuantity(Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be positive"
            );
        }
        this.quantity = quantity;
        calculateSubtotal();
    }

    public void changeUnitPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Price cannot be negative"
            );
        }
        this.unitPrice = price;
        calculateSubtotal();
    }

    private void calculateSubtotal() {
        this.subtotal =
                unitPrice.multiply(
                        BigDecimal.valueOf(quantity)
                );
    }
}