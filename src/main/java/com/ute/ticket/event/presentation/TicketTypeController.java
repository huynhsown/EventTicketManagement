package com.ute.ticket.event.presentation;

import com.ute.ticket.event.application.facade.TicketTypeFacade;
import com.ute.ticket.event.application.result.TicketTypeResult;
import com.ute.ticket.event.presentation.dto.CreateTicketTypeRequest;
import com.ute.ticket.event.presentation.mapper.CreateTicketTypeMapper;
import com.ute.ticket.shared.application.security.CurrentUser;
import com.ute.ticket.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sessions/{sessionId}/ticket-types")
@RequiredArgsConstructor
@Tag(name = "Ticket Type", description = "Ticket type endpoints")
public class TicketTypeController {

    private final TicketTypeFacade ticketTypeFacade;
    private final CreateTicketTypeMapper createTicketTypeMapper;
    private final CurrentUser currentUser;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a ticket type for a session (owner / admin)")
    public ApiResponse<TicketTypeResult> create(
            @PathVariable Long sessionId,
            @Valid @RequestBody CreateTicketTypeRequest request
    ) {
        var command = createTicketTypeMapper.toCommand(sessionId, currentUser.getUserId(), request);
        var result = ticketTypeFacade.createTicketType(command);
        return ApiResponse.<TicketTypeResult>builder()
                .success(true)
                .message("Ticket type created successfully")
                .data(result)
                .build();
    }
}
