package com.ute.ticket.event.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArchiveCategoryCommand {

    private Long id;
}
