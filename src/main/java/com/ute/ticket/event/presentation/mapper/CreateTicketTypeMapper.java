package com.ute.ticket.event.presentation.mapper;

import com.ute.ticket.event.application.command.CreateTicketTypeCommand;
import com.ute.ticket.event.presentation.dto.CreateTicketTypeRequest;
import org.springframework.stereotype.Component;

@Component
public class CreateTicketTypeMapper {

    public CreateTicketTypeCommand toCommand(Long sessionId, Long userId, CreateTicketTypeRequest request) {
        return CreateTicketTypeCommand.builder()
                .sessionId(sessionId)
                .userId(userId)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .maxPerUser(request.getMaxPerUser())
                .build();
    }
}
