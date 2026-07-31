package com.ute.ticket.order.presentation.mapper;

import com.ute.ticket.order.application.command.CreateOrderCommand;
import com.ute.ticket.order.presentation.dto.CreateOrderRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderPresentationMapper {

    public CreateOrderCommand toCommand(CreateOrderRequest request, Long userId) {
        return CreateOrderCommand.builder()
                .userId(userId)
                .sessionId(request.getSessionId())
                .ticketTypeId(request.getTicketTypeId())
                .quantity(request.getQuantity())
                .build();
    }
}