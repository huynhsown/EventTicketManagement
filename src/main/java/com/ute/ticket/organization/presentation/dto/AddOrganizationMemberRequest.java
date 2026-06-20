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
@Schema(description = "Add organization member request payload")
@NoArgsConstructor
@AllArgsConstructor
public class AddOrganizationMemberRequest {

    @NotNull
    @Schema(description = "Id of the user to add", example = "1")
    private Long userId;

    @NotNull
    @Schema(description = "Member role", example = "MEMBER")
    private MemberRole role;
}
