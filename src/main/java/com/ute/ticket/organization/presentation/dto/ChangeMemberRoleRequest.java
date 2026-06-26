package com.ute.ticket.organization.presentation.dto;

import com.ute.ticket.organization.domain.enums.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@Setter
@Schema(description = "Change member role request payload")
@NoArgsConstructor
@AllArgsConstructor
public class ChangeMemberRoleRequest {

    @NotNull
    @Schema(description = "New member role (ADMIN or MEMBER; OWNER routes to Transfer Ownership)", example = "ADMIN")
    private MemberRole role;
}
