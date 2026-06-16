package com.ute.ticket.organization.presentation;

import com.ute.ticket.organization.application.facade.OrganizationFacade;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    private final OrganizationFacade organizationFacade;

    private final CreateOrganizationMapper createOrganizationMapper;
    private final CurrentUser currentUser;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new organization")
    public ApiResponse<OrganizationResult> create(@Valid @RequestBody CreateOrganizationRequest request) {
        var command = createOrganizationMapper.toCommand(request, currentUser.getUserId());
        var result = organizationFacade.createOrganization(command);
        return ApiResponse.<OrganizationResult>builder()
                .success(true)
                .message("Organization created successfully")
                .data(result)
                .build();
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Get organization detail by slug (active organizations only)")
    public ApiResponse<OrganizationResult> getBySlug(@PathVariable String slug) {
        var result = organizationFacade.getOrganization(slug);
        return ApiResponse.<OrganizationResult>builder()
                .success(true)
                .message("Organization retrieved successfully")
                .data(result)
                .build();
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate an organization (system admin)")
    public ApiResponse<OrganizationResult> activate(@PathVariable Long id) {
        var result = organizationFacade.activateOrganization(id);
        return ApiResponse.<OrganizationResult>builder()
                .success(true)
                .message("Organization activated successfully")
                .data(result)
                .build();
    }

    @PatchMapping("/{id}/suspend")
    @Operation(summary = "Suspend an organization (system admin)")
    public ApiResponse<OrganizationResult> suspend(@PathVariable Long id) {
        var result = organizationFacade.suspendOrganization(id);
        return ApiResponse.<OrganizationResult>builder()
                .success(true)
                .message("Organization suspended successfully")
                .data(result)
                .build();
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate an organization (owner)")
    public ApiResponse<OrganizationResult> deactivate(@PathVariable Long id) {
        var result = organizationFacade.deactivateOrganization(id, currentUser.getUserId());
        return ApiResponse.<OrganizationResult>builder()
                .success(true)
                .message("Organization deactivated successfully")
                .data(result)
                .build();
    }
}
