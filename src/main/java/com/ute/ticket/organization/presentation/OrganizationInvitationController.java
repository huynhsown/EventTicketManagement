package com.ute.ticket.organization.presentation;

import com.ute.ticket.organization.application.facade.OrganizationFacade;
import com.ute.ticket.organization.application.result.OrganizationMemberResult;
import com.ute.ticket.shared.application.security.CurrentUser;
import com.ute.ticket.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organizations/invitations")
@RequiredArgsConstructor
@Tag(name = "Organization Invitation", description = "Organization invitation endpoints")
public class OrganizationInvitationController {

    private final OrganizationFacade organizationFacade;
    private final CurrentUser currentUser;

    @PostMapping("/accept")
    @Operation(summary = "Accept an organization invitation (invitee)")
    public ApiResponse<OrganizationMemberResult> acceptInvitation(@RequestParam String token) {
        var result = organizationFacade.acceptInvitation(token, currentUser.getUserId());
        return ApiResponse.<OrganizationMemberResult>builder()
                .success(true)
                .message("Invitation accepted successfully")
                .data(result)
                .build();
    }

    @PostMapping("/reject")
    @Operation(summary = "Reject an organization invitation (invitee)")
    public ApiResponse<OrganizationMemberResult> rejectInvitation(@RequestParam String token) {
        var result = organizationFacade.rejectInvitation(token, currentUser.getUserId());
        return ApiResponse.<OrganizationMemberResult>builder()
                .success(true)
                .message("Invitation rejected successfully")
                .data(result)
                .build();
    }
}
