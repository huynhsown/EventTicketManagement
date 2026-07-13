package com.ute.ticket.event.presentation.mapper;

import com.ute.ticket.event.application.command.ChangeEventCategoryCommand;
import com.ute.ticket.event.presentation.dto.ChangeEventCategoryRequest;
import org.springframework.stereotype.Component;

@Component
public class ChangeEventCategoryMapper {

    public ChangeEventCategoryCommand toCommand(Long eventId, Long userId, ChangeEventCategoryRequest request) {
        return ChangeEventCategoryCommand.builder()
                .eventId(eventId)
                .userId(userId)
                .categoryIds(request.getCategoryIds())
                .build();
    }
}
