package com.ute.ticket.order.domain.entity;

import com.ute.ticket.shared.domain.BaseDomain;
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