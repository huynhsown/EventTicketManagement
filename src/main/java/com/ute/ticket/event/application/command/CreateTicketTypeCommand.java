package com.ute.ticket.event.application.command;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CreateTicketTypeCommand {

    private Long sessionId;
    private Long userId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer maxPerUser;
}
