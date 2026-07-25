package com.ute.ticket.shared.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration

public class RabbitMQConfig {
    public static final String EVENT_EXCHANGE = "event";

    public static final String EVENT_CREATED_QUEUE = "event.created.queue";
    public static final String EVENT_PUBLISHED_QUEUE = "event.published.queue";
    public static final String EVENT_VENUE_ASSIGN_QUEUE = "event.venue-assigned.queue";

    public static final String EVENT_CREATED_KEY = "event.created";
    public static final String EVENT_PUBLISHED_KEY = "event.published";
    public static final String EVENT_VENUE_ASSIGN_KEY = "event.venue-assigned";

    @Bean
    public TopicExchange eventExchange() {
        return new TopicExchange(EVENT_EXCHANGE, true, false);
    }

    @Bean
    public Queue eventCreateQueue() {
        return new Queue(EVENT_CREATED_QUEUE, true);
    }

    @Bean
    public Queue eventPublishQueue() {
        return new Queue(EVENT_PUBLISHED_QUEUE, true);
    }

    @Bean
    public Queue eventVenueAssignQueue() {
        return new Queue(EVENT_VENUE_ASSIGN_QUEUE, true);
    }

    @Bean
    public Binding eventCreatedBinding(
            Queue eventCreateQueue,
            TopicExchange eventExchange) {
        return BindingBuilder.bind(eventCreateQueue)
                .to(eventExchange)
                .with(EVENT_CREATED_KEY);
    }

    @Bean
    public Binding eventPublishedBinding(
            Queue eventPublishQueue,
            TopicExchange eventExchange) {
        return BindingBuilder.bind(eventPublishQueue)
                .to(eventExchange)
                .with(EVENT_PUBLISHED_KEY);
    }

    @Bean
    public Binding eventVenueAssignBinding(
            Queue eventVenueAssignQueue,
            TopicExchange eventExchange) {
        return BindingBuilder.bind(eventVenueAssignQueue)
                .to(eventExchange)
                .with(EVENT_VENUE_ASSIGN_KEY);
    }
}

