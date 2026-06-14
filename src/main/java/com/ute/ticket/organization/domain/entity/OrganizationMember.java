package com.ute.ticket.organization.domain.entity;

import com.ute.ticket.organization.domain.enums.MemberRole;
import com.ute.ticket.organization.domain.enums.MemberStatus;
import com.ute.ticket.shared.domain.BaseDomain;
import com.ute.ticket.shared.exception.DomainConflictException;
import com.ute.ticket.shared.exception.DomainValidationException;
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

    public static OrganizationMember create(
            Long organizationId,
            Long userId,
            MemberRole role
    ) {
        if (organizationId == null) {
            throw new DomainValidationException("Organization id cannot be null.");
        }

        if (userId == null) {
            throw new DomainValidationException("User id cannot be null.");
        }

        if (role == null) {
            throw new DomainValidationException("Member role cannot be null.");
        }

        return OrganizationMember.builder()
                .organizationId(organizationId)
                .userId(userId)
                .role(role)
                .status(MemberStatus.PENDING)
                .joinedAt(Instant.now())
                .build();
    }

    public void activate() {
        if (status == MemberStatus.ACTIVE) {
            throw new DomainConflictException("Member is already active.");
        }

        if (status == MemberStatus.REMOVED) {
            throw new DomainConflictException("Removed member cannot be activated.");
        }

        status = MemberStatus.ACTIVE;
    }

    public void suspend() {
        if (status == MemberStatus.SUSPENDED) {
            throw new DomainConflictException("Member is already suspended.");
        }

        if (status == MemberStatus.PENDING) {
            throw new DomainConflictException("Pending member cannot be suspended.");
        }

        if (status == MemberStatus.REMOVED) {
            throw new DomainConflictException("Removed member cannot be suspended.");
        }

        status = MemberStatus.SUSPENDED;
    }

    public void remove() {
        if (status == MemberStatus.REMOVED) {
            throw new DomainConflictException("Member is already removed.");
        }

        status = MemberStatus.REMOVED;
    }

    public void changeRole(MemberRole newRole) {
        if (newRole == null) {
            throw new DomainValidationException("Member role cannot be null.");
        }

        if (status == MemberStatus.REMOVED) {
            throw new DomainConflictException("Removed member cannot change role.");
        }

        if (isOwner()) {
            throw new DomainConflictException("Owner role must be transferred before it can be changed.");
        }

        role = newRole;
    }

    public void leave() {
        if (isOwner()) {
            throw new DomainConflictException("Owner cannot leave. Transfer ownership first.");
        }

        if (status == MemberStatus.REMOVED) {
            throw new DomainConflictException("Member has already left.");
        }

        status = MemberStatus.REMOVED;
    }

    public boolean isOwner() {
        return role == MemberRole.OWNER;
    }

    public boolean isAdmin() {
        return role == MemberRole.OWNER || role == MemberRole.ADMIN;
    }
}
