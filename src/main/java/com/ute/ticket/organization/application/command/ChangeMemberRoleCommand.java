package com.ute.ticket.organization.application.command;

import com.ute.ticket.organization.domain.enums.MemberRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChangeMemberRoleCommand {

    private Long organizationId;
    private Long userId;
    private Long targetUserId;
    private MemberRole role;
}
