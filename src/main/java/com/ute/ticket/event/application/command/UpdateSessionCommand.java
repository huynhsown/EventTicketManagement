package com.ute.ticket.event.application.command;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class UpdateSessionCommand {

    private Long sessionId;
    private Long eventId;
    private Long userId;
    private Instant startTime;
    private Instant endTime;
    private Instant salesStartAt;
    private Instant salesEndAt;
}
