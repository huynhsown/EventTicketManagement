package com.ute.ticket.event.presentation.mapper;

import com.ute.ticket.event.application.command.InitializeInventoryCommand;
import com.ute.ticket.event.presentation.dto.InitializeInventoryRequest;
import org.springframework.stereotype.Component;

@Component
public class InitializeInventoryMapper {

    public InitializeInventoryCommand toCommand(Long ticketTypeId, Long userId, InitializeInventoryRequest request) {
        return InitializeInventoryCommand.builder()
                .ticketTypeId(ticketTypeId)
                .userId(userId)
                .quantity(request.getQuantity())
                .build();
    }
}
