package com.ute.ticket.venue.application.command;

import com.ute.ticket.venue.domain.enums.VenueStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CreateVenueCommand {

    private String name;
    private String address;
    private String city;
    private String country;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer capacity;
    private String description;
    private VenueStatus status;
}
