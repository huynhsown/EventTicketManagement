package com.ute.ticket.event.application.command;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class CreateEventCommand {

    private Long organizationId;
    private Long userId;
    private String title;
    private String description;
    private Long venueId;
    private Set<Long> categoryIds;
    private String bannerUrl;
}
