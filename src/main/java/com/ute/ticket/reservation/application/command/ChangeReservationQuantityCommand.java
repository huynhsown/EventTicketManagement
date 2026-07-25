package com.ute.ticket.reservation.application.command;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ChangeReservationQuantityCommand {
    private UUID id;
    private Long userId;
    private Integer quantity;
}
