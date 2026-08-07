package com.ute.ticket.order.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateOrderCommand {

    private Long userId;
    private Long sessionId;
    private Long ticketTypeId;
    private Integer quantity;
}