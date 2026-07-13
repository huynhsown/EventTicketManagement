package com.ute.ticket.event.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InitializeInventoryCommand {

    private Long ticketTypeId;
    private Long userId;
    private Integer quantity;
}
