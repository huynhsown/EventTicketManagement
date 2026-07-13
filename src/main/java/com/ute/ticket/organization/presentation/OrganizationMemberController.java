package com.ute.ticket.organization.presentation;

import com.ute.ticket.organization.application.facade.OrganizationFacade;
import com.ute.ticket.organization.application.result.OrganizationMemberResult;
import com.ute.ticket.organization.presentation.dto.AddOrganizationMemberRequest;
import com.ute.ticket.organization.presentation.dto.ChangeMemberRoleRequest;
import com.ute.ticket.organization.presentation.mapper.OrganizationPresentationMapper;
import com.ute.ticket.shared.application.security.CurrentUser;
import com.ute.ticket.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@Tag(name = "Organization Member", description = "Organization member endpoints")
public class OrganizationMemberController {

    private final OrganizationFacade organizationFacade;
    private final OrganizationPresentationMapper organizationPresentationMapper;
    private final CurrentUser currentUser;

    @PostMapping("/{id}/members")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a member to an organization (admin/owner)")
    public ApiResponse<OrganizationMemberResult> addMember(
            @PathVariable Long id,
            @Valid @RequestBody AddOrganizationMemberRequest request
    ) {
        var command = organizationPresentationMapper.toCommand(id, request, currentUser.getUserId());
        var result = organizationFacade.addOrganizationMember(command);
        return ApiResponse.<OrganizationMemberResult>builder()
                .success(true)
                .message("Organization member added successfully")
                .data(result)
                .build();
    }

    @DeleteMapping("/{id}/members/{userId}")
    @Operation(summary = "Remove a member from an organization (owner/admin)")
    public ApiResponse<OrganizationMemberResult> removeMember(
            @PathVariable Long id,
            @PathVariable Long userId
    ) {
        var result = organizationFacade.removeOrganizationMember(id, userId, currentUser.getUserId());
        return ApiResponse.<OrganizationMemberResult>builder()
                .success(true)
                .message("Organization member removed successfully")
                .data(result)
                .build();
    }

    @PatchMapping("/{id}/members/{targetUserId}/role")
    @Operation(summary = "Change a member's role (owner)")
    public ApiResponse<OrganizationMemberResult> changeMemberRole(
            @PathVariable Long id,
            @PathVariable Long targetUserId,
            @Valid @RequestBody ChangeMemberRoleRequest request
    ) {
        var command = organizationPresentationMapper.toCommand(id, targetUserId, request, currentUser.getUserId());
        var result = organizationFacade.changeMemberRole(command);
        return ApiResponse.<OrganizationMemberResult>builder()
                .success(true)
                .message("Member role changed successfully")
                .data(result)
                .build();
    }

    @PostMapping("/{id}/leave")
    @Operation(summary = "Leave an organization (member)")
    public ApiResponse<OrganizationMemberResult> leave(@PathVariable Long id) {
        var result = organizationFacade.leaveOrganization(id, currentUser.getUserId());
        return ApiResponse.<OrganizationMemberResult>builder()
                .success(true)
                .message("Left organization successfully")
                .data(result)
                .build();
    }
}
