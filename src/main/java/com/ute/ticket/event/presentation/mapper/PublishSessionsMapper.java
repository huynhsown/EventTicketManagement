package com.ute.ticket.event.presentation.mapper;

import com.ute.ticket.event.application.command.PublishSessionsCommand;
import com.ute.ticket.event.presentation.dto.PublishSessionsRequest;
import org.springframework.stereotype.Component;

@Component
public class PublishSessionsMapper {

    public PublishSessionsCommand toCommand(Long eventId, Long userId, PublishSessionsRequest request) {
        return PublishSessionsCommand.builder()
                .eventId(eventId)
                .userId(userId)
                .sessionIds(request.getSessionIds())
                .build();
    }
}