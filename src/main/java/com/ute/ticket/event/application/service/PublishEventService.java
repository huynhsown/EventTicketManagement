package com.ute.ticket.event.application.service;

import com.ute.ticket.event.application.command.PublishEventCommand;
import com.ute.ticket.event.application.command.VerifyEventReadyForPublishingCommand;
import com.ute.ticket.event.application.port.in.PublishEventUseCase;
import com.ute.ticket.event.application.port.in.VerifyEventReadyForPublishingUseCase;
import com.ute.ticket.event.application.port.out.EventRepository;
import com.ute.ticket.event.application.result.EventReadinessResult;
import com.ute.ticket.event.application.result.EventReadinessResult.ReadinessCheck;
import com.ute.ticket.event.application.result.EventResult;
import com.ute.ticket.event.domain.entity.Event;
import com.ute.ticket.event.domain.enums.EventStatus;
import com.ute.ticket.event.domain.event.EventPublished;
import com.ute.ticket.organization.application.port.out.OrganizationMemberRepository;
import com.ute.ticket.shared.application.event.EventPublisher;
import com.ute.ticket.shared.exception.ConflictException;
import com.ute.ticket.shared.exception.ForbiddenException;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PublishEventService implements PublishEventUseCase {

    private final EventRepository eventRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final VerifyEventReadyForPublishingUseCase verifyEventReadyForPublishingUseCase;
    private final EventPublisher eventPublisher;

    @Override
    public EventResult execute(PublishEventCommand cmd) {
        Event event = eventRepository.findActiveById(cmd.getEventId())
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (!organizationMemberRepository.existsAdminById(event.getOrganizationId(), cmd.getUserId())) {
            throw new ForbiddenException("Only organization owner or admin can publish events");
        }

        if (event.getStatus() == EventStatus.PUBLISHED) {
            throw new ConflictException("Event is already published");
        }

        if (event.getStatus() != EventStatus.DRAFT) {
            throw new ConflictException("Only draft events can be published");
        }

        List<ReadinessCheck> failedChecks = verifyEventReadyForPublishingUseCase.execute(
                        VerifyEventReadyForPublishingCommand.builder()
                                .eventId(event.getId())
                                .userId(cmd.getUserId())
                                .build()
                )
                .checks()
                .stream()
                .filter(check -> !check.passed())
                .toList();

        if (!failedChecks.isEmpty()) {
            String details = failedChecks.stream()
                    .map(check -> check.name() + ": " + check.message())
                    .collect(Collectors.joining("; "));
            throw new ConflictException("Event is not ready to be published: " + details);
        }

        event.publish();
        event = eventRepository.save(event);

        eventPublisher.publishEventPublished(new EventPublished(
                event.getId(),
                event.getStatus(),
                event.getPublishedAt()
        ));

        return EventResult.from(event);
    }
}
