package com.ute.ticket.event.infrastructure.persistence;

import com.ute.ticket.event.domain.entity.Inventory;
import com.ute.ticket.event.domain.entity.TicketType;
import com.ute.ticket.event.domain.enums.InventoryStatus;
import com.ute.ticket.event.domain.enums.SessionStatus;
import com.ute.ticket.event.domain.enums.TicketTypeStatus;
import com.ute.ticket.event.infrastructure.persistence.adapter.InventoryRepositoryAdapter;
import com.ute.ticket.event.infrastructure.persistence.adapter.TicketTypeRepositoryAdapter;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.InventoryJpaEntity;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.SessionJpaEntity;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.TicketTypeJpaEntity;
import com.ute.ticket.event.infrastructure.persistence.jpa.mapper.InventoryMapper;
import com.ute.ticket.event.infrastructure.persistence.jpa.mapper.TicketTypeMapper;
import com.ute.ticket.event.infrastructure.persistence.jpa.repository.InventoryJpaRepository;
import com.ute.ticket.event.infrastructure.persistence.jpa.repository.SessionJpaRepository;
import com.ute.ticket.event.infrastructure.persistence.jpa.repository.TicketTypeJpaRepository;
import com.ute.ticket.shared.config.JpaAuditConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@EnableJpaRepositories(basePackages = "com.ute.ticket.event.infrastructure.persistence.jpa.repository")
@EntityScan(basePackages = "com.ute.ticket.event.infrastructure.persistence.jpa.entity")
@Import({TicketTypeRepositoryAdapter.class, InventoryRepositoryAdapter.class,
        TicketTypeMapper.class, InventoryMapper.class, JpaAuditConfig.class})
class EventDetailReadRepositoriesTest {

    private static final Instant BASE = Instant.parse("2026-09-01T08:00:00Z");

    @Autowired private SessionJpaRepository sessionJpaRepository;
    @Autowired private TicketTypeJpaRepository ticketTypeJpaRepository;
    @Autowired private InventoryJpaRepository inventoryJpaRepository;
    @Autowired private TicketTypeRepositoryAdapter ticketTypeRepositoryAdapter;
    @Autowired private InventoryRepositoryAdapter inventoryRepositoryAdapter;

    @Test
    void findBySessionIdsInReturnsTicketTypesForAllSessions() {
        Long sessionA = sessionJpaRepository.save(session(1L)).getId();
        Long sessionB = sessionJpaRepository.save(session(2L)).getId();
        ticketTypeJpaRepository.save(ticketType(sessionA, "Alpha", "10.00"));
        ticketTypeJpaRepository.save(ticketType(sessionA, "Beta", "20.00"));
        ticketTypeJpaRepository.save(ticketType(sessionB, "Gamma", "30.00"));

        List<TicketType> result = ticketTypeRepositoryAdapter.findBySessionIdsIn(List.of(sessionA, sessionB));

        assertThat(result).hasSize(3);
        assertThat(result).extracting(TicketType::getSessionId)
                .containsExactlyInAnyOrder(sessionA, sessionA, sessionB);
        assertThat(result).extracting(TicketType::getName)
                .containsExactlyInAnyOrder("Alpha", "Beta", "Gamma");
    }

    @Test
    void findBySessionIdsInReturnsEmptyForUnknownSessions() {
        assertThat(ticketTypeRepositoryAdapter.findBySessionIdsIn(List.of(999L))).isEmpty();
    }

    @Test
    void findByIdsInReturnsInventoriesForAllTicketTypes() {
        Long ticketA = ticketTypeJpaRepository.save(ticketType(1L, "Alpha", "10.00")).getId();
        Long ticketB = ticketTypeJpaRepository.save(ticketType(2L, "Beta", "20.00")).getId();
        inventoryJpaRepository.save(inventory(ticketA, 100, 0, 20));
        inventoryJpaRepository.save(inventory(ticketB, 500, 0, 50));

        List<Inventory> result = inventoryRepositoryAdapter.findByIdsIn(List.of(ticketA, ticketB));

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Inventory::getTicketTypeId).containsExactlyInAnyOrder(ticketA, ticketB);
        assertThat(result).extracting(Inventory::getSoldStock).containsExactlyInAnyOrder(20, 50);
    }

    @Test
    void findByIdsInReturnsEmptyForUnknownTicketTypes() {
        assertThat(inventoryRepositoryAdapter.findByIdsIn(List.of(999L))).isEmpty();
    }

    private SessionJpaEntity session(Long eventId) {
        return SessionJpaEntity.builder()
                .eventId(eventId)
                .startTime(BASE)
                .endTime(BASE.plusSeconds(3600))
                .salesStartAt(BASE.minusSeconds(3600))
                .salesEndAt(BASE)
                .status(SessionStatus.LIVE)
                .build();
    }

    private TicketTypeJpaEntity ticketType(Long sessionId, String name, String price) {
        return TicketTypeJpaEntity.builder()
                .sessionId(sessionId)
                .name(name)
                .description("desc")
                .price(new BigDecimal(price))
                .maxPerUser(2)
                .status(TicketTypeStatus.ACTIVE)
                .build();
    }

    private InventoryJpaEntity inventory(Long ticketTypeId, int total, int reserved, int sold) {
        return InventoryJpaEntity.builder()
                .ticketTypeId(ticketTypeId)
                .totalStock(total)
                .reservedStock(reserved)
                .soldStock(sold)
                .status(InventoryStatus.ACTIVE)
                .build();
    }
}
