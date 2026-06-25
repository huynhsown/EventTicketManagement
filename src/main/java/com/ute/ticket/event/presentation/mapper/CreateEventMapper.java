package com.ute.ticket.event.presentation.mapper;

import com.ute.ticket.event.application.command.CreateEventCommand;
import com.ute.ticket.event.presentation.dto.CreateEventRequest;
import org.springframework.stereotype.Component;

@Component
public class CreateEventMapper {

    public CreateEventCommand toCommand(CreateEventRequest request, Long userId) {
        return CreateEventCommand.builder()
                .organizationId(request.getOrganizationId())
                .userId(userId)
                .title(request.getTitle())
                .description(request.getDescription())
                .venueId(request.getVenueId())
                .categoryIds(request.getCategoryIds())
                .bannerUrl(request.getBannerUrl())
                .build();
    }
}
