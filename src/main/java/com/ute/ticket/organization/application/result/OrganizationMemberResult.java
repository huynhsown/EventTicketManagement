package com.ute.ticket.organization.application.result;

import com.ute.ticket.organization.domain.entity.OrganizationMember;
import com.ute.ticket.organization.domain.enums.MemberRole;
import com.ute.ticket.organization.domain.enums.MemberStatus;

import java.time.Instant;

public record OrganizationMemberResult(
        Long organizationId,
        Long userId,
        MemberRole role,
        MemberStatus status,
        Instant joinedAt
) {
    public static OrganizationMemberResult from(OrganizationMember member) {
        return new OrganizationMemberResult(
                member.getOrganizationId(),
                member.getUserId(),
                member.getRole(),
                member.getStatus(),
                member.getJoinedAt()
        );
    }
}
