package com.ute.ticket.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "event")
public record EventProperties(Integer maxCategoriesPerEvent, Boolean autoCreateInventory) {

    public EventProperties {
        maxCategoriesPerEvent = maxCategoriesPerEvent == null ? 10 : maxCategoriesPerEvent;
        autoCreateInventory = autoCreateInventory == null ? Boolean.TRUE : autoCreateInventory;
    }
}
