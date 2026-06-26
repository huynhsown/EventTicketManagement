package com.ute.ticket.organization.application.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ute.ticket.identity.application.port.out.UserRepository;
import com.ute.ticket.identity.domain.entity.User;
import com.ute.ticket.organization.application.command.AddOrganizationMemberCommand;
import com.ute.ticket.organization.application.port.in.InviteOrganizationMemberUseCase;
import com.ute.ticket.organization.application.port.out.OrganizationInvitationRepository;
import com.ute.ticket.organization.application.port.out.OrganizationMemberRepository;
import com.ute.ticket.organization.application.port.out.OrganizationRepository;
import com.ute.ticket.organization.application.result.OrganizationMemberResult;
import com.ute.ticket.organization.domain.entity.Organization;
import com.ute.ticket.organization.domain.entity.OrganizationInvitation;
import com.ute.ticket.organization.domain.entity.OrganizationMember;
import com.ute.ticket.organization.domain.enums.MemberRole;
import com.ute.ticket.shared.exception.ConflictException;
import com.ute.ticket.shared.exception.ForbiddenException;
import com.ute.ticket.shared.exception.NotFoundException;
import com.ute.ticket.shared.utils.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class InviteOrganizationMemberService implements InviteOrganizationMemberUseCase {

    private static final long INVITATION_EXPIRE_DAYS = 3;

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final OrganizationInvitationRepository invitationRepository;
    private final UserRepository userRepository;

    @Override
    public OrganizationMemberResult execute(AddOrganizationMemberCommand cmd) {

        if (cmd.getRole() == MemberRole.OWNER) {
            throw new ForbiddenException("User cannot add OWNER role to new member");
        }

        User user = userRepository.findById(cmd.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Organization organization = organizationRepository.findById(cmd.getOrganizationId())
                .orElseThrow(() -> new NotFoundException("Organization not found"));

        if (!organization.isActive()) {
            throw new ConflictException("Organization isn't active");
        }

        OrganizationMember actor = organizationMemberRepository
                .findById(cmd.getOrganizationId(), cmd.getAddedBy())
                .orElseThrow(() -> new ForbiddenException(
                        "Only organization admins or owners can add members"));

        actor.ensureCanManageMembers();

        OrganizationMember member = organizationMemberRepository
                .findByOrganizationIdAndUserId(cmd.getOrganizationId(), cmd.getUserId())
                .orElse(null);

        if (member == null) {
            member = OrganizationMember.create(
                    organization.getId(),
                    cmd.getUserId(),
                    cmd.getRole()
            );
        } else if (member.isRemoved()) {
            member.pending();
            member.changeRole(cmd.getRole());
        } else {
            throw new ConflictException("User is already a member of this organization.");
        }

        member = organizationMemberRepository.save(member);

        createInvitation(
                organization.getId(),
                user.getEmail(),
                cmd.getRole(),
                actor.getUserId()
        );
        return OrganizationMemberResult.from(member);
    }

    private void createInvitation(
            Long organizationId,
            String email,
            MemberRole role,
            Long invitedBy
    ) {
        OrganizationInvitation invitation = OrganizationInvitation.create(
                UuidCreator.getTimeOrderedEpoch(),
                organizationId,
                email,
                role,
                invitedBy,
                TokenGenerator.generate(),
                Instant.now().plus(INVITATION_EXPIRE_DAYS, ChronoUnit.DAYS)
        );

        invitationRepository.save(invitation);
    }
}
