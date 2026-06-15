package com.ute.ticket.organization.domain.entity;

import com.ute.ticket.organization.domain.enums.InvitationStatus;
import com.ute.ticket.organization.domain.enums.MemberRole;
import com.ute.ticket.shared.domain.BaseDomain;
import com.ute.ticket.shared.exception.DomainConflictException;
import com.ute.ticket.shared.exception.DomainValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Getter
@SuperBuilder
@AllArgsConstructor
public class OrganizationInvitation extends BaseDomain {

    private final UUID id;
    private final Long organizationId;
    private String email;
    private MemberRole role;
    private final Long invitedBy;
    private final String token;
    private InvitationStatus status;
    private final Instant expiresAt;

    public static OrganizationInvitation create(
            UUID id,
            Long organizationId,
            String email,
            MemberRole role,
            Long invitedBy,
            String token,
            Instant expiresAt
    ) {
        if (id == null) {
            throw new DomainValidationException("Invitation id cannot be null.");
        }

        if (organizationId == null) {
            throw new DomainValidationException("Organization id cannot be null.");
        }

        if (email == null || email.isBlank()) {
            throw new DomainValidationException("Invitation email cannot be blank.");
        }

        if (role == null) {
            throw new DomainValidationException("Invitation role cannot be null.");
        }

        if (invitedBy == null) {
            throw new DomainValidationException("InvitedBy cannot be null.");
        }

        if (token == null || token.isBlank()) {
            throw new DomainValidationException("Invitation token cannot be blank.");
        }

        if (expiresAt == null) {
            throw new DomainValidationException("Invitation expiresAt cannot be null.");
        }

        Instant now = Instant.now();

        return OrganizationInvitation.builder()
                .id(id)
                .organizationId(organizationId)
                .email(email.trim().toLowerCase())
                .role(role)
                .invitedBy(invitedBy)
                .token(token)
                .status(InvitationStatus.PENDING)
                .expiresAt(expiresAt)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public boolean isPending() {
        return status == InvitationStatus.PENDING;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public boolean isTokenValid(String token) {
        return this.token.equals(token);
    }

    public void accept(String token) {

        ensurePending();
        ensureNotExpired();

        if (!isTokenValid(token)) {
            throw new DomainConflictException("Invalid invitation token.");
        }

        status = InvitationStatus.ACCEPTED;
    }

    public void reject() {

        ensurePending();
        ensureNotExpired();

        status = InvitationStatus.REJECTED;
    }

    public void cancel() {

        ensurePending();

        status = InvitationStatus.CANCELLED;
    }

    public void changeRole(MemberRole role) {

        ensurePending();
        ensureNotExpired();

        this.role = role;
    }

    public void changeEmail(String email) {

        ensurePending();
        ensureNotExpired();

        if (email == null || email.isBlank()) {
            throw new DomainValidationException("Invitation email cannot be blank.");
        }

        this.email = email.trim().toLowerCase();
    }

    public boolean canResend() {
        return isPending() && isExpired();
    }

    private void ensurePending() {

        if (status != InvitationStatus.PENDING) {
            throw new DomainConflictException("Invitation is no longer pending.");
        }
    }

    private void ensureNotExpired() {

        if (isExpired()) {
            throw new DomainConflictException("Invitation has expired.");
        }
    }
}
