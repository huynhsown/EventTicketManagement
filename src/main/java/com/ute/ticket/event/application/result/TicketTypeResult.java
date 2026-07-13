package com.ute.ticket.event.application.result;

import com.ute.ticket.event.domain.entity.TicketType;
import com.ute.ticket.event.domain.enums.TicketTypeStatus;

import java.math.BigDecimal;

public record TicketTypeResult(
        Long id,
        Long sessionId,
        String name,
        String description,
        BigDecimal price,
        Integer maxPerUser,
        TicketTypeStatus status
) {
    public static TicketTypeResult from(TicketType ticketType) {
        return new TicketTypeResult(
                ticketType.getId(),
                ticketType.getSessionId(),
                ticketType.getName(),
                ticketType.getDescription(),
                ticketType.getPrice(),
                ticketType.getMaxPerUser(),
                ticketType.getStatus()
        );
    }
}
