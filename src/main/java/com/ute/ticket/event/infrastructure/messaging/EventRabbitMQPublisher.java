package com.ute.ticket.event.infrastructure.messaging;

import com.ute.ticket.event.domain.event.EventCreated;
import com.ute.ticket.event.domain.event.EventPublished;
import com.ute.ticket.event.domain.event.EventVenueAssigned;
import com.ute.ticket.shared.application.event.EventPublisher;
import com.ute.ticket.shared.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventRabbitMQPublisher implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishEventCreated(EventCreated event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EVENT_EXCHANGE,
                RabbitMQConfig.EVENT_CREATED_KEY,
                event
        );
    }

    @Override
    public void publishEventPublished(EventPublished event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EVENT_EXCHANGE,
                RabbitMQConfig.EVENT_PUBLISHED_KEY,
                event
        );
    }

    @Override
    public void publishEventVenueAssigned(EventVenueAssigned event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EVENT_EXCHANGE,
                RabbitMQConfig.EVENT_VENUE_ASSIGN_KEY,
                event
        );
    }
}
