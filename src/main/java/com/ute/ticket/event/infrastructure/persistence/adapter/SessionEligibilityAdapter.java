package com.ute.ticket.event.infrastructure.persistence.adapter;

import com.ute.ticket.event.application.port.out.SessionEligibilityPort;
import com.ute.ticket.event.domain.enums.InventoryStatus;
import com.ute.ticket.event.domain.enums.SessionStatus;
import com.ute.ticket.event.infrastructure.persistence.jpa.repository.SessionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
@RequiredArgsConstructor
public class SessionEligibilityAdapter implements SessionEligibilityPort {

    private static final SessionStatus PUBLISHED_STATUS = SessionStatus.PUBLISHED;
    private static final InventoryStatus ACTIVE_INVENTORY_STATUS = InventoryStatus.ACTIVE;

    private final SessionJpaRepository sessionJpaRepository;

    @Override
    public boolean isEligibleForPurchase(Long sessionId, Long ticketTypeId, int quantity) {
        return sessionJpaRepository.existsEligibleForPurchase(
                sessionId,
                ticketTypeId,
                PUBLISHED_STATUS,
                ACTIVE_INVENTORY_STATUS,
                Instant.now(),
                quantity
        );
    }
}