package com.ute.ticket.organization.domain.entity;

import com.ute.ticket.organization.domain.enums.MemberRole;
import com.ute.ticket.organization.domain.enums.MemberStatus;
import com.ute.ticket.shared.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@SuperBuilder
@AllArgsConstructor
public class OrganizationMember extends BaseDomain {

    private final Long organizationId;
    private final Long userId;
    private MemberRole role;
    private MemberStatus status;
    private Instant joinedAt;


}
