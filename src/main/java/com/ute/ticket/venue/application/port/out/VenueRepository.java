package com.ute.ticket.venue.application.port.out;

import com.ute.ticket.shared.dto.PageInfo;
import com.ute.ticket.venue.domain.entity.Venue;
import com.ute.ticket.venue.domain.enums.VenueStatus;

import java.util.Optional;

public interface VenueRepository {

    Venue save(Venue venue);

    Optional<Venue> findById(Long id);

    PageInfo<Venue> search(
            String keyword,
            String city,
            VenueStatus status,
            int page,
            int size,
            String sortBy,
            boolean ascending
    );

    void deleteById(Long id);
}
