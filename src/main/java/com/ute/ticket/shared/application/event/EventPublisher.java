package com.ute.ticket.shared.application.event;

import com.ute.ticket.event.domain.event.EventCreated;
import com.ute.ticket.event.domain.event.EventPublished;
import com.ute.ticket.event.domain.event.EventVenueAssigned;

public interface EventPublisher {

    void publishEventCreated(EventCreated event);
    void publishEventPublished(EventPublished event);
    void publishEventVenueAssigned(EventVenueAssigned event);
}
