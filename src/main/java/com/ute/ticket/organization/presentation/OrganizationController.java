package com.ute.ticket.organization.presentation;

import com.ute.ticket.organization.application.port.in.CreateOrganizationUseCase;
import com.ute.ticket.organization.application.result.OrganizationResult;
import com.ute.ticket.organization.presentation.dto.CreateOrganizationRequest;
import com.ute.ticket.organization.presentation.mapper.CreateOrganizationMapper;
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
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
@Tag(name = "Organization", description = "Organization endpoints")
public class OrganizationController {

    private final CreateOrganizationUseCase createOrganizationUseCase;
    private final CreateOrganizationMapper createOrganizationMapper;
    private final CurrentUser currentUser;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new organization")
    public ApiResponse<OrganizationResult> create(@Valid @RequestBody CreateOrganizationRequest request) {
        var command = createOrganizationMapper.toCommand(request, currentUser.getUserId());
        var result = createOrganizationUseCase.execute(command);
        return ApiResponse.<OrganizationResult>builder()
                .success(true)
                .message("Organization created successfully")
                .data(result)
                .build();
    }
}
