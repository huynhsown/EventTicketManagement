package com.ute.ticket.event.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateCategoryCommand {
    private String name;
    private String slug;
    private String description;
    private Integer displayOrder;
}
