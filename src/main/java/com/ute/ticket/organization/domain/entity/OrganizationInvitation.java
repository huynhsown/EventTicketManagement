package com.ute.ticket.organization.domain.entity;

import com.ute.ticket.shared.domain.BaseDomain;
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
    private String role;
    private final Long invitedBy;
    private final String token;
    private String status;
    private final Instant expiresAt;

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public boolean isTokenValid(String token) {
        return this.token.equals(token);
    }

    public void accept() {
        this.status = "ACCEPTED";
    }

    public void reject() {
        this.status = "REJECTED";
    }
}
