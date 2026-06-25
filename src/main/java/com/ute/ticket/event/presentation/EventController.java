package com.ute.ticket.event.presentation;

import com.ute.ticket.event.application.facade.EventFacade;
import com.ute.ticket.event.application.result.EventResult;
import com.ute.ticket.event.presentation.dto.CreateEventRequest;
import com.ute.ticket.event.presentation.mapper.CreateEventMapper;
import com.ute.ticket.shared.application.security.CurrentUser;
import com.ute.ticket.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Event", description = "Event endpoints")
public class EventController {

    private final EventFacade eventFacade;
    private final CreateEventMapper createEventMapper;
    private final CurrentUser currentUser;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new draft event (owner / admin)")
    public ApiResponse<EventResult> create(@Valid @RequestBody CreateEventRequest request) {
        var command = createEventMapper.toCommand(request, currentUser.getUserId());
        var result = eventFacade.createEvent(command);
        return ApiResponse.<EventResult>builder()
                .success(true)
                .message("Event created successfully")
                .data(result)
                .build();
    }
}
