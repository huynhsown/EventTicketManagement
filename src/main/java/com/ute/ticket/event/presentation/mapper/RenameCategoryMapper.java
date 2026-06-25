package com.ute.ticket.event.presentation.mapper;

import com.ute.ticket.event.application.command.RenameCategoryCommand;
import com.ute.ticket.event.presentation.dto.RenameCategoryRequest;
import org.springframework.stereotype.Component;

@Component
public class RenameCategoryMapper {

    public RenameCategoryCommand toCommand(Long id, RenameCategoryRequest request) {
        return RenameCategoryCommand.builder()
                .id(id)
                .name(request.getName())
                .slug(request.getSlug())
                .build();
    }
}
