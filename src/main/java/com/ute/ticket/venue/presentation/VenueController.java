package com.ute.ticket.venue.presentation;

import com.ute.ticket.shared.dto.ApiResponse;
import com.ute.ticket.shared.dto.PageInfo;
import com.ute.ticket.venue.application.port.in.ChangeVenueStatusUseCase;
import com.ute.ticket.venue.application.port.in.CreateVenueUseCase;
import com.ute.ticket.venue.application.port.in.DeleteVenueUseCase;
import com.ute.ticket.venue.application.port.in.GetVenueUseCase;
import com.ute.ticket.venue.application.port.in.SearchVenueUseCase;
import com.ute.ticket.venue.application.port.in.UpdateVenueUseCase;
import com.ute.ticket.venue.application.result.VenueResult;
import com.ute.ticket.venue.domain.enums.VenueStatus;
import com.ute.ticket.venue.presentation.dto.ChangeVenueStatusRequest;
import com.ute.ticket.venue.presentation.dto.CreateVenueRequest;
import com.ute.ticket.venue.presentation.dto.UpdateVenueRequest;
import com.ute.ticket.venue.presentation.mapper.ChangeVenueStatusMapper;
import com.ute.ticket.venue.presentation.mapper.CreateVenueMapper;
import com.ute.ticket.venue.presentation.mapper.SearchVenueMapper;
import com.ute.ticket.venue.presentation.mapper.UpdateVenueMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
@Tag(name = "Venue", description = "Venue endpoints")
public class VenueController {

    private final CreateVenueUseCase createVenueUseCase;
    private final UpdateVenueUseCase updateVenueUseCase;
    private final GetVenueUseCase getVenueUseCase;
    private final SearchVenueUseCase searchVenueUseCase;
    private final ChangeVenueStatusUseCase changeVenueStatusUseCase;
    private final DeleteVenueUseCase deleteVenueUseCase;
    private final CreateVenueMapper createVenueMapper;
    private final UpdateVenueMapper updateVenueMapper;
    private final SearchVenueMapper searchVenueMapper;
    private final ChangeVenueStatusMapper changeVenueStatusMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new venue")
    public ApiResponse<VenueResult> create(@Valid @RequestBody CreateVenueRequest request) {
        var command = createVenueMapper.toCommand(request);
        var result = createVenueUseCase.execute(command);
        return ApiResponse.<VenueResult>builder()
                .success(true)
                .message("Venue created successfully")
                .data(result)
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a venue")
    public ApiResponse<VenueResult> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVenueRequest request
    ) {
        var command = updateVenueMapper.toCommand(id, request);
        var result = updateVenueUseCase.execute(command);
        return ApiResponse.<VenueResult>builder()
                .success(true)
                .message("Venue updated successfully")
                .data(result)
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get venue detail")
    public ApiResponse<VenueResult> getById(@PathVariable Long id) {
        var result = getVenueUseCase.execute(id);
        return ApiResponse.<VenueResult>builder()
                .success(true)
                .message("Venue retrieved successfully")
                .data(result)
                .build();
    }

    @GetMapping
    @Operation(summary = "Search venues")
    public ApiResponse<PageInfo<VenueResult>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) VenueStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {
        var command = searchVenueMapper.toCommand(keyword, city, status, page, size, sortBy, ascending);
        var result = searchVenueUseCase.execute(command);
        return ApiResponse.<PageInfo<VenueResult>>builder()
                .success(true)
                .message("Venues retrieved successfully")
                .data(result)
                .build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change venue status")
    public ApiResponse<VenueResult> changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChangeVenueStatusRequest request
    ) {
        var command = changeVenueStatusMapper.toCommand(id, request);
        var result = changeVenueStatusUseCase.execute(command);
        return ApiResponse.<VenueResult>builder()
                .success(true)
                .message("Venue status changed successfully")
                .data(result)
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a venue")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        deleteVenueUseCase.execute(id);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Venue deleted successfully")
                .build();
    }
}
