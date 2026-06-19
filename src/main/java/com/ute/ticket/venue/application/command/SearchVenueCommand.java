package com.ute.ticket.venue.application.command;

import com.ute.ticket.venue.domain.enums.VenueStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchVenueCommand {

    private String keyword;
    private String city;
    private VenueStatus status;
    private int page;
    private int size;
    private String sortBy;
    private boolean ascending;
}
