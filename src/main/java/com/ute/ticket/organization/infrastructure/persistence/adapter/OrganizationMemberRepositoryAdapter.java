package com.ute.ticket.organization.infrastructure.persistence.adapter;

import com.ute.ticket.organization.application.port.out.OrganizationMemberRepository;
import com.ute.ticket.organization.domain.entity.OrganizationMember;
import com.ute.ticket.organization.domain.enums.MemberRole;
import com.ute.ticket.organization.domain.enums.MemberStatus;
import com.ute.ticket.organization.infrastructure.persistence.jpa.entity.OrganizationMemberJpaEntity;
import com.ute.ticket.organization.infrastructure.persistence.jpa.entity.OrganizationMemberJpaEntity.OrganizationMemberId;
import com.ute.ticket.organization.infrastructure.persistence.jpa.mapper.OrganizationMemberMapper;
import com.ute.ticket.organization.infrastructure.persistence.jpa.repository.OrganizationMemberJpaRepository;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrganizationMemberRepositoryAdapter implements OrganizationMemberRepository {

    private final OrganizationMemberJpaRepository organizationMemberJpaRepository;
    private final OrganizationMemberMapper organizationMemberMapper;

    @Override
    public OrganizationMember save(OrganizationMember member) {
        OrganizationMemberId id = new OrganizationMemberId(member.getOrganizationId(), member.getUserId());
        OrganizationMemberJpaEntity jpaEntity;
        if (!organizationMemberJpaRepository.existsById(id)) {
            jpaEntity = organizationMemberMapper.toJpaEntity(member);
        } else {
            jpaEntity = organizationMemberJpaRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Organization member not found"));
            organizationMemberMapper.updateEntity(jpaEntity, member);
        }
        OrganizationMemberJpaEntity saved = organizationMemberJpaRepository.save(jpaEntity);
        return organizationMemberMapper.toDomain(saved);
    }

    @Override
    public Optional<OrganizationMember> findById(Long organizationId, Long userId) {
        OrganizationMemberId id = new OrganizationMemberId(organizationId, userId);
        return organizationMemberJpaRepository.findById(id)
                .map(organizationMemberMapper::toDomain);
    }

    @Override
    public List<OrganizationMember> findByOrganizationId(Long organizationId) {
        return organizationMemberJpaRepository.findByOrganizationId(organizationId)
                .stream()
                .map(organizationMemberMapper::toDomain)
                .toList();
    }

    @Override
    public List<OrganizationMember> findByOrganizationIdAndStatus(Long organizationId, MemberStatus status) {
        return organizationMemberJpaRepository.findByOrganizationIdAndStatus(organizationId, status)
                .stream()
                .map(organizationMemberMapper::toDomain)
                .toList();
    }

    @Override
    public List<OrganizationMember> findByUserId(Long userId) {
        return organizationMemberJpaRepository.findByUserId(userId)
                .stream()
                .map(organizationMemberMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(Long organizationId, Long userId) {
        OrganizationMemberId id = new OrganizationMemberId(organizationId, userId);
        return organizationMemberJpaRepository.existsById(id);
    }

    @Override
    public boolean existsAdminById(Long organizationId, Long userId) {
        return organizationMemberJpaRepository.existsByOrganizationIdAndUserIdAndRoleIn(
                organizationId,
                userId,
                List.of(MemberRole.OWNER, MemberRole.ADMIN)
        );
    }

    @Override
    public void deleteById(Long organizationId, Long userId) {
        OrganizationMemberId id = new OrganizationMemberId(organizationId, userId);
        organizationMemberJpaRepository.deleteById(id);
    }
}
