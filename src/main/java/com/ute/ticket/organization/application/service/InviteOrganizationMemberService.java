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
import com.ute.ticket.shared.exception.BadRequestException;
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

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final OrganizationInvitationRepository invitationRepository;
    private final UserRepository userRepository;

    @Override
    public OrganizationMemberResult execute(AddOrganizationMemberCommand cmd) {

        User user = userRepository.findById(cmd.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Organization organization = organizationRepository.findById(cmd.getOrganizationId())
                .orElseThrow(() -> new NotFoundException("Organization not found"));

        if (!organization.isActive()) {
            throw new ConflictException("Organization isn't active");
        }

        OrganizationMember actor = organizationMemberRepository.findById(cmd.getOrganizationId(), cmd.getAddedBy())
                .orElseThrow(() -> new ForbiddenException("Only organization admins or owner can add members"));

        actor.ensureCanManageMembers();

        if (organizationMemberRepository.existsById(cmd.getOrganizationId(), cmd.getUserId())) {
            throw new ConflictException("User is already a member of this organization");
        }

        OrganizationMember member = OrganizationMember.create(
                organization.getId(),
                cmd.getUserId(),
                cmd.getRole()
        );
        member = organizationMemberRepository.save(member);

        OrganizationInvitation invitation = OrganizationInvitation.create(
                UuidCreator.getTimeOrderedEpoch(),
                organization.getId(),
                user.getEmail(),
                cmd.getRole(),
                actor.getUserId(),
                TokenGenerator.generate(),
                Instant.now().plus(3, ChronoUnit.DAYS)
        );

        invitation = invitationRepository.save(invitation);

        // Will publish message for noti & email at here

        return OrganizationMemberResult.from(member);
    }
}
