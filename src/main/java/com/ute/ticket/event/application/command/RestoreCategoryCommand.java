package com.ute.ticket.event.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestoreCategoryCommand {

    private Long id;
}
