package com.ute.ticket.venue.application.result;

import com.ute.ticket.venue.domain.entity.Venue;
import com.ute.ticket.venue.domain.enums.VenueStatus;

import java.math.BigDecimal;

public record VenueResult(
        Long id,
        String name,
        String address,
        String city,
        String country,
        BigDecimal latitude,
        BigDecimal longitude,
        Integer capacity,
        String description,
        VenueStatus status
) {
    public static VenueResult from(Venue venue) {
        return new VenueResult(
                venue.getId(),
                venue.getName(),
                venue.getAddress(),
                venue.getCity(),
                venue.getCountry(),
                venue.getLatitude(),
                venue.getLongitude(),
                venue.getCapacity(),
                venue.getDescription(),
                venue.getStatus()
        );
    }
}
