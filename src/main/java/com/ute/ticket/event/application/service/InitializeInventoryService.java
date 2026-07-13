package com.ute.ticket.event.application.service;

import com.ute.ticket.event.application.command.InitializeInventoryCommand;
import com.ute.ticket.event.application.port.in.InitializeInventoryUseCase;
import com.ute.ticket.event.application.port.out.EventRepository;
import com.ute.ticket.event.application.port.out.InventoryRepository;
import com.ute.ticket.event.application.port.out.InventoryTransactionRepository;
import com.ute.ticket.event.application.port.out.SessionRepository;
import com.ute.ticket.event.application.port.out.TicketTypeRepository;
import com.ute.ticket.event.application.result.InventoryResult;
import com.ute.ticket.event.domain.entity.Event;
import com.ute.ticket.event.domain.entity.Inventory;
import com.ute.ticket.event.domain.entity.InventoryTransaction;
import com.ute.ticket.event.domain.entity.Session;
import com.ute.ticket.event.domain.entity.TicketType;
import com.ute.ticket.organization.application.port.out.OrganizationMemberRepository;
import com.ute.ticket.shared.exception.BadRequestException;
import com.ute.ticket.shared.exception.ConflictException;
import com.ute.ticket.shared.exception.ForbiddenException;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InitializeInventoryService implements InitializeInventoryUseCase {

    private final TicketTypeRepository ticketTypeRepository;
    private final SessionRepository sessionRepository;
    private final EventRepository eventRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final OrganizationMemberRepository organizationMemberRepository;

    @Override
    public InventoryResult execute(InitializeInventoryCommand cmd) {
        TicketType ticketType = ticketTypeRepository.findActiveById(cmd.getTicketTypeId())
                .orElseThrow(() -> new BadRequestException("Ticket type does not exist or is not active"));

        Session session = sessionRepository.findActiveById(ticketType.getSessionId())
                .orElseThrow(() -> new NotFoundException("Session not found"));

        Event event = eventRepository.findActiveById(session.getEventId())
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (!organizationMemberRepository.existsAdminById(event.getOrganizationId(), cmd.getUserId())) {
            throw new ForbiddenException("Only organization owner or admin can initialize inventory");
        }

        if (cmd.getQuantity() == null || cmd.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }

        if (inventoryRepository.existsByTicketTypeId(ticketType.getId())) {
            throw new ConflictException("Inventory already initialized for this ticket type");
        }

        Inventory inventory = Inventory.create(ticketType.getId(), cmd.getQuantity());
        InventoryTransaction transaction = inventory.initializationTransaction("Initial inventory");

        inventory = inventoryRepository.save(inventory);
        inventoryTransactionRepository.save(transaction);

        return InventoryResult.from(inventory);
    }
}
