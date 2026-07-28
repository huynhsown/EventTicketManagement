package com.ute.ticket.event.application.command;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PublishSessionsCommand {

    private Long eventId;
    private Long userId;
    private List<Long> sessionIds;
}