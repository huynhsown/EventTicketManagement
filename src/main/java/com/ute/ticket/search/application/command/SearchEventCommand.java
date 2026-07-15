package com.ute.ticket.search.application.command;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchEventCommand {

    private String keyword;
    private String city;
    private List<String> categorySlugs;
    private String status;
    private Double minPrice;
    private Double maxPrice;
    private Boolean hasAvailableTickets;
    private int page;
    private int size;
    private String sortBy;
    private boolean ascending;
}
