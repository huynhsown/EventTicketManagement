package com.ute.ticket.order.application.result;

import com.ute.ticket.order.domain.entity.Order;
import com.ute.ticket.order.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderResult(
        Long id,
        String code,
        Long userId,
        OrderStatus status,
        BigDecimal totalAmount,
        List<Item> items
) {

    public static OrderResult from(Order order) {
        return new OrderResult(
                order.getId(),
                order.getCode(),
                order.getUserId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getItems().stream()
                        .map(item -> new Item(
                                item.getTicketTypeId(),
                                item.getQuantity(),
                                item.getUnitPrice(),
                                item.getSubtotal()
                        ))
                        .toList()
        );
    }

    public record Item(
            Long ticketTypeId,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal
    ) {
    }
}