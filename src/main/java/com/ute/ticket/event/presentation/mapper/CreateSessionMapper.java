package com.ute.ticket.event.presentation.mapper;

import com.ute.ticket.event.application.command.CreateSessionCommand;
import com.ute.ticket.event.presentation.dto.CreateSessionRequest;
import org.springframework.stereotype.Component;

@Component
public class CreateSessionMapper {

    public CreateSessionCommand toCommand(Long eventId, Long userId, CreateSessionRequest request) {
        return CreateSessionCommand.builder()
                .eventId(eventId)
                .userId(userId)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .salesStartAt(request.getSalesStartAt())
                .salesEndAt(request.getSalesEndAt())
                .build();
    }
}
