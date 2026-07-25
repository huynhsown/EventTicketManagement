package com.ute.ticket.reservation.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateReservationCommand {

    private Long userId;
    private Long ticketTypeId;
    private Integer quantity;
}
