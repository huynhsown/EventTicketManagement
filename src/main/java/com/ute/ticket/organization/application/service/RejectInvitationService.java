package com.ute.ticket.organization.application.service;

import com.ute.ticket.identity.application.port.out.UserRepository;
import com.ute.ticket.identity.domain.entity.User;
import com.ute.ticket.organization.application.command.RejectInvitationCommand;
import com.ute.ticket.organization.application.port.in.RejectInvitationUseCase;
import com.ute.ticket.organization.application.port.out.OrganizationInvitationRepository;
import com.ute.ticket.organization.application.port.out.OrganizationMemberRepository;
import com.ute.ticket.organization.application.result.OrganizationMemberResult;
import com.ute.ticket.organization.domain.entity.OrganizationInvitation;
import com.ute.ticket.organization.domain.entity.OrganizationMember;
import com.ute.ticket.shared.exception.ForbiddenException;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RejectInvitationService implements RejectInvitationUseCase {

    private final OrganizationInvitationRepository invitationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final UserRepository userRepository;

    @Override
    public OrganizationMemberResult execute(RejectInvitationCommand cmd) {

        OrganizationInvitation invitation = invitationRepository.findByToken(cmd.getToken())
                .orElseThrow(() -> new NotFoundException("Invitation not found"));

        invitation.reject();
        invitation = invitationRepository.save(invitation);

        OrganizationMember member = organizationMemberRepository.findById(invitation.getOrganizationId(), cmd.getUserId())
                .orElseThrow(() -> new NotFoundException("Organization member not found"));

        member.remove();
        member = organizationMemberRepository.save(member);

        return OrganizationMemberResult.from(member);
    }
}
