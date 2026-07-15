package com.ute.ticket.search.presentation;

import com.ute.ticket.search.application.port.in.SearchEventUseCase;
import com.ute.ticket.search.application.result.EventSearchResult;
import com.ute.ticket.search.presentation.mapper.SearchEventMapper;
import com.ute.ticket.shared.dto.ApiResponse;
import com.ute.ticket.shared.dto.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/events/search")
@RequiredArgsConstructor
@Tag(name = "Event Search", description = "Public event search endpoints")
public class SearchEventController {

    private final SearchEventUseCase searchEventUseCase;
    private final SearchEventMapper searchEventMapper;

    @GetMapping
    @Operation(summary = "Search and filter published events")
    public ApiResponse<PageInfo<EventSearchResult>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) List<String> categorySlugs,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean hasAvailableTickets,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {
        var command = searchEventMapper.toCommand(
                keyword, city, categorySlugs, status, minPrice, maxPrice, hasAvailableTickets,
                page, size, sortBy, ascending
        );
        var result = searchEventUseCase.execute(command);
        return ApiResponse.<PageInfo<EventSearchResult>>builder()
                .success(true)
                .message("Events retrieved successfully")
                .data(result)
                .build();
    }
}
