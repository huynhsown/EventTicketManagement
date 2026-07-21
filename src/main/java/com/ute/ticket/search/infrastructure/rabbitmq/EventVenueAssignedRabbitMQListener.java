package com.ute.ticket.search.infrastructure.rabbitmq;

import com.ute.ticket.event.domain.event.EventVenueAssigned;
import com.ute.ticket.search.application.port.out.EventIndexer;
import com.ute.ticket.shared.config.RabbitMQConfig;
import com.ute.ticket.venue.application.port.out.VenueRepository;
import com.ute.ticket.venue.domain.entity.Venue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventVenueAssignedRabbitMQListener {

    private final EventIndexer eventIndexer;
    private final VenueRepository venueRepository;

    @RabbitListener(queues = RabbitMQConfig.EVENT_VENUE_ASSIGN_QUEUE)
    public void onEventVenueAssigned(EventVenueAssigned event) {
        Venue venue = venueRepository.findById(event.venueId()).orElse(null);
        if (venue == null) {
            log.warn("Venue [{}] not found for event [{}], skipping venue update", event.venueId(), event.eventId());
            return;
        }

        eventIndexer.updateVenue(
                event.eventId(),
                venue.getId(),
                venue.getName(),
                venue.getCity()
        );
        log.info("Updated venue of event [{}] to [{}] in search", event.eventId(), venue.getName());
    }
}
