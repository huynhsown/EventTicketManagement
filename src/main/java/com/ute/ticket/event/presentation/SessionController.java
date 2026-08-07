package com.ute.ticket.event.presentation;

import com.ute.ticket.event.application.facade.SessionFacade;
import com.ute.ticket.event.application.port.in.PreSalePreloadUseCase;
import com.ute.ticket.event.application.result.SessionResult;
import com.ute.ticket.event.presentation.dto.CreateSessionRequest;
import com.ute.ticket.event.presentation.dto.PublishSessionsRequest;
import com.ute.ticket.event.presentation.mapper.CreateSessionMapper;
import com.ute.ticket.event.presentation.mapper.PublishSessionsMapper;
import com.ute.ticket.shared.application.security.CurrentUser;
import com.ute.ticket.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events/{eventId}/sessions")
@RequiredArgsConstructor
@Tag(name = "Session", description = "Session endpoints")
public class SessionController {

    private final SessionFacade sessionFacade;
    private final CreateSessionMapper createSessionMapper;
    private final PublishSessionsMapper publishSessionsMapper;
    private final CurrentUser currentUser;
    private final PreSalePreloadUseCase preSalePreloadUseCase;

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

    @PostMapping("/publish")
    @Operation(summary = "Publish batch sessions of a published event (owner / admin)")
    public ApiResponse<List<SessionResult>> publishSessions(
            @PathVariable Long eventId,
            @Valid @RequestBody PublishSessionsRequest request
    ) {
        var command = publishSessionsMapper.toCommand(eventId, currentUser.getUserId(), request);
        var result = sessionFacade.publishSessions(command);
        return ApiResponse.<List<SessionResult>>builder()
                .success(true)
                .message("Sessions published successfully")
                .data(result)
                .build();
    }

    @GetMapping
    public ApiResponse<?> test() {
        preSalePreloadUseCase.execute();
        return ApiResponse.builder()
                .success(true)
                .message("Session list successfully")
                .data(null)
                .build();
    }
}
