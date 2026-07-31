package com.ute.ticket.order.application.facade;

import com.ute.ticket.order.application.command.CreateOrderCommand;
import com.ute.ticket.order.application.port.in.CreateOrderUseCase;
import com.ute.ticket.order.application.result.OrderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final CreateOrderUseCase createOrderUseCase;

    public OrderResult createOrder(CreateOrderCommand command) {
        return createOrderUseCase.execute(command);
    }
}