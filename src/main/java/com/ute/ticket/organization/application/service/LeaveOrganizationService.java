package com.ute.ticket.organization.application.service;

import com.ute.ticket.organization.application.command.LeaveOrganizationCommand;
import com.ute.ticket.organization.application.port.in.LeaveOrganizationUseCase;
import com.ute.ticket.organization.application.port.out.OrganizationMemberRepository;
import com.ute.ticket.organization.application.result.OrganizationMemberResult;
import com.ute.ticket.organization.domain.entity.OrganizationMember;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveOrganizationService implements LeaveOrganizationUseCase {

    private final OrganizationMemberRepository organizationMemberRepository;

    @Override
    public OrganizationMemberResult execute(LeaveOrganizationCommand cmd) {
        OrganizationMember member = organizationMemberRepository.findById(cmd.getOrganizationId(), cmd.getUserId())
                .orElseThrow(() -> new NotFoundException("You are not a member"));

        member.leave();
        member = organizationMemberRepository.save(member);
        return OrganizationMemberResult.from(member);
    }
}
