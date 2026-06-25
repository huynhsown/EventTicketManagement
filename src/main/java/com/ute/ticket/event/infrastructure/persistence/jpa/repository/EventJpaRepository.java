package com.ute.ticket.event.infrastructure.persistence.jpa.repository;

import com.ute.ticket.event.domain.enums.EventStatus;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.EventJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventJpaRepository extends JpaRepository<EventJpaEntity, Long> {
    List<EventJpaEntity> findByOrganizationId(Long organizationId);
    List<EventJpaEntity> findByOrganizationIdAndStatus(Long organizationId, EventStatus status);
    List<EventJpaEntity> findByStatus(EventStatus status);
    boolean existsByOrganizationIdAndTitle(Long organizationId, String title);
}
