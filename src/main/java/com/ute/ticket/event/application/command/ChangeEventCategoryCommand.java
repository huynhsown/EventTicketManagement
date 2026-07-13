package com.ute.ticket.event.application.command;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class ChangeEventCategoryCommand {

    private Long eventId;
    private Long userId;
    private Set<Long> categoryIds;
}
