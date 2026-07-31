package com.ute.ticket.order.application.service;

import com.ute.ticket.event.application.port.out.InventoryReleasePort;
import com.ute.ticket.event.application.port.out.InventoryReservePort;
import com.ute.ticket.event.application.port.out.SessionEligibilityPort;
import com.ute.ticket.event.application.port.out.TicketTypeRepository;
import com.ute.ticket.event.domain.entity.TicketType;
import com.ute.ticket.order.application.command.CreateOrderCommand;
import com.ute.ticket.order.application.port.in.CreateOrderUseCase;
import com.ute.ticket.order.application.port.out.OrderRepository;
import com.ute.ticket.order.application.result.OrderResult;
import com.ute.ticket.order.domain.entity.Order;
import com.ute.ticket.order.domain.entity.OrderItem;
import com.ute.ticket.reservation.application.port.out.CreateReservationPort;
import com.ute.ticket.shared.exception.BadRequestException;
import com.ute.ticket.shared.exception.ConflictException;
import com.ute.ticket.shared.exception.NotFoundException;
import com.ute.ticket.shared.utils.UuidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateOrderService implements CreateOrderUseCase {

    private final SessionEligibilityPort sessionEligibilityPort;
    private final InventoryReservePort inventoryReservePort;
    private final InventoryReleasePort inventoryReleasePort;
    private final TicketTypeRepository ticketTypeRepository;
    private final CreateReservationPort createReservationPort;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResult execute(CreateOrderCommand cmd) {
        boolean reserved = false;
        try {
            if (!sessionEligibilityPort.isEligibleForPurchase(
                    cmd.getSessionId(),
                    cmd.getTicketTypeId(),
                    cmd.getQuantity()
            )) {
                throw new ConflictException(
                        "Session " + cmd.getSessionId()
                                + " is not eligible for purchase."
                );
            }

            TicketType ticketType = ticketTypeRepository.findActiveById(cmd.getTicketTypeId())
                    .orElseThrow(() -> new NotFoundException(
                            "Ticket type " + cmd.getTicketTypeId() + " not found"
                    ));

            reserved = inventoryReservePort.reserve(
                    cmd.getTicketTypeId(),
                    cmd.getQuantity()
            );

            if (!reserved) {
                throw new ConflictException("Insufficient stock.");
            }

            var reservation = createReservationPort.create(
                    cmd.getUserId(),
                    cmd.getTicketTypeId(),
                    cmd.getQuantity()
            );

            OrderItem orderItem = OrderItem.create(
                    null,
                    cmd.getTicketTypeId(),
                    cmd.getQuantity(),
                    ticketType.getPrice()
            );

            Order order = Order.create(
                    UuidGenerator.v7().toString(),
                    reservation.id(),
                    cmd.getUserId(),
                    List.of(orderItem)
            );

            order = orderRepository.save(order);
            return OrderResult.from(order);
        } catch (Exception e) {
            if (reserved) {
                inventoryReleasePort.release(cmd.getTicketTypeId(), cmd.getQuantity());
            }
            throw e;
        }
    }
}