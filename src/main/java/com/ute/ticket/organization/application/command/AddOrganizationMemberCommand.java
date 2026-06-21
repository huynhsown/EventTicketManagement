package com.ute.ticket.organization.application.command;

import com.ute.ticket.organization.domain.enums.MemberRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddOrganizationMemberCommand {

    private Long organizationId;
    private Long userId;
    private MemberRole role;
    private Long addedBy;
}
