package com.ute.ticket.event.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateCategoryCommand {

    private Long id;
    private String description;
    private Integer displayOrder;
}
