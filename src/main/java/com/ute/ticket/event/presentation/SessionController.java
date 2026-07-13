package com.ute.ticket.event.presentation;

import com.ute.ticket.event.application.facade.SessionFacade;
import com.ute.ticket.event.application.result.SessionResult;
import com.ute.ticket.event.presentation.dto.CreateSessionRequest;
import com.ute.ticket.event.presentation.mapper.CreateSessionMapper;
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
@RequestMapping("/api/events/{eventId}/sessions")
@RequiredArgsConstructor
@Tag(name = "Session", description = "Session endpoints")
public class SessionController {

    private final SessionFacade sessionFacade;
    private final CreateSessionMapper createSessionMapper;
    private final CurrentUser currentUser;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new scheduled session (owner / admin)")
    public ApiResponse<SessionResult> create(
            @PathVariable Long eventId,
            @Valid @RequestBody CreateSessionRequest request
    ) {
        var command = createSessionMapper.toCommand(eventId, currentUser.getUserId(), request);
        var result = sessionFacade.createSession(command);
        return ApiResponse.<SessionResult>builder()
                .success(true)
                .message("Session created successfully")
                .data(result)
                .build();
    }
}
