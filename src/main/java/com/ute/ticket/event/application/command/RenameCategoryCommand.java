package com.ute.ticket.event.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RenameCategoryCommand {

    private Long id;
    private String name;
    private String slug;
}
