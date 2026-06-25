package com.ute.ticket.event.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteCategoryCommand {

    private Long id;
}
