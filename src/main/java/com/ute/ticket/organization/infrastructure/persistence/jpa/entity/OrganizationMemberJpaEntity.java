package com.ute.ticket.organization.infrastructure.persistence.jpa.entity;

import com.ute.ticket.organization.domain.enums.MemberRole;
import com.ute.ticket.organization.domain.enums.MemberStatus;
import com.ute.ticket.shared.config.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "organization_members")
@IdClass(OrganizationMemberJpaEntity.OrganizationMemberId.class)
public class OrganizationMemberJpaEntity extends BaseEntity {

    @Id
    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 30, nullable = false)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private MemberStatus status;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class OrganizationMemberId implements Serializable {
        private Long organizationId;
        private Long userId;
    }
}
