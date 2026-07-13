package com.ute.ticket.event.application.port.in;

import com.ute.ticket.event.application.command.VerifyEventReadyForPublishingCommand;
import com.ute.ticket.event.application.result.EventReadinessResult;

public interface VerifyEventReadyForPublishingUseCase {
    EventReadinessResult execute(VerifyEventReadyForPublishingCommand cmd);
}
