package com.ute.ticket.order.application.port.in;

import com.ute.ticket.order.application.command.CreateOrderCommand;
import com.ute.ticket.order.application.result.OrderResult;

public interface CreateOrderUseCase {
    OrderResult execute(CreateOrderCommand cmd);
}