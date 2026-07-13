package com.ute.ticket.event.application.facade;

import com.ute.ticket.event.application.command.AssignVenueCommand;
import com.ute.ticket.event.application.command.CreateEventCommand;
import com.ute.ticket.event.application.command.PublishEventCommand;
import com.ute.ticket.event.application.command.VerifyEventReadyForPublishingCommand;
import com.ute.ticket.event.application.port.in.AssignVenueUseCase;
import com.ute.ticket.event.application.port.in.CreateEventUseCase;
import com.ute.ticket.event.application.port.in.PublishEventUseCase;
import com.ute.ticket.event.application.port.in.VerifyEventReadyForPublishingUseCase;
import com.ute.ticket.event.application.result.EventReadinessResult;
import com.ute.ticket.event.application.result.EventResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventFacade {

    private final CreateEventUseCase createEventUseCase;
    private final AssignVenueUseCase assignVenueUseCase;
    private final VerifyEventReadyForPublishingUseCase verifyEventReadyForPublishingUseCase;
    private final PublishEventUseCase publishEventUseCase;

    public EventResult createEvent(CreateEventCommand cmd) {
        return createEventUseCase.execute(cmd);
    }

    public EventResult assignVenue(AssignVenueCommand cmd) {
        return assignVenueUseCase.execute(cmd);
    }

    public EventReadinessResult verifyEventReadyForPublishing(Long eventId, Long userId) {
        var command = VerifyEventReadyForPublishingCommand.builder()
                .eventId(eventId)
                .userId(userId)
                .build();
        return verifyEventReadyForPublishingUseCase.execute(command);
    }

    public EventResult publishEvent(Long eventId, Long userId) {
        var command = PublishEventCommand.builder()
                .eventId(eventId)
                .userId(userId)
                .build();
        return publishEventUseCase.execute(command);
    }
}
