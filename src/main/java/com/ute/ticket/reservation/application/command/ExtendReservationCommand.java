package com.ute.ticket.reservation.application.command;

import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.util.UUID;

@Getter
@Builder
public class ExtendReservationCommand {
    private UUID id;
    private Long userId;
    private Duration extension;
}
