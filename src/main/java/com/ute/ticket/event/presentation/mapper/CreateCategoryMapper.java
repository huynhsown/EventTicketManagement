package com.ute.ticket.event.presentation.mapper;

import com.ute.ticket.event.application.command.CreateCategoryCommand;
import com.ute.ticket.event.presentation.dto.CreateCategoryRequest;
import org.springframework.stereotype.Component;

@Component
public class CreateCategoryMapper {

    public CreateCategoryCommand toCommand(CreateCategoryRequest request) {
        return CreateCategoryCommand.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .displayOrder(request.getDisplayOrder())
                .build();
    }
}
