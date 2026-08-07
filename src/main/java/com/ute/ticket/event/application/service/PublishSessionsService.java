package com.ute.ticket.event.application.service;

import com.ute.ticket.event.application.command.PublishSessionsCommand;
import com.ute.ticket.event.application.port.in.PublishSessionsUseCase;
import com.ute.ticket.event.application.port.out.EventRepository;
import com.ute.ticket.event.application.port.out.SessionRepository;
import com.ute.ticket.event.application.result.SessionResult;
import com.ute.ticket.event.domain.entity.Event;
import com.ute.ticket.event.domain.entity.Session;
import com.ute.ticket.event.domain.enums.EventStatus;
import com.ute.ticket.organization.application.port.out.OrganizationMemberRepository;
import com.ute.ticket.shared.exception.ConflictException;
import com.ute.ticket.shared.exception.ForbiddenException;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PublishSessionsService implements PublishSessionsUseCase {

    private final EventRepository eventRepository;
    private final SessionRepository sessionRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    @Override
    public List<SessionResult> execute(PublishSessionsCommand cmd) {
        Event event = eventRepository.findActiveById(cmd.getEventId())
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (!organizationMemberRepository.existsAdminById(event.getOrganizationId(), cmd.getUserId())) {
            throw new ForbiddenException("Only organization owner or admin can publish sessions");
        }

        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new ConflictException("Sessions can only be published after the event has been published");
        }

        List<Long> sessionIds = new ArrayList<>(new LinkedHashSet<>(cmd.getSessionIds()));
        if (sessionIds.isEmpty()) {
            throw new ConflictException("At least one session id is required");
        }

        Map<Long, Session> sessionsById = sessionRepository.findByIds(sessionIds).stream()
                .collect(Collectors.toMap(Session::getId, Function.identity()));

        List<Session> sessions = sessionIds.stream()
                .map(sessionsById::get)
                .filter(Objects::nonNull)
                .toList();

        if (sessions.size() != sessionIds.size()) {
            throw new NotFoundException("One or more sessions do not exist");
        }

        for (Session session : sessions) {
            session.ensureBelongsToEvent(event.getId());
            session.publish();
        }
        sessions = sessionRepository.saveAll(sessions);

        return sessions.stream()
                .map(SessionResult::from)
                .toList();
    }
}