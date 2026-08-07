package com.ute.ticket.order.application.service;

import com.ute.ticket.event.application.port.out.InventoryReservationPort;
import com.ute.ticket.event.application.port.out.InventoryReservePort;
import com.ute.ticket.event.application.port.out.TicketTypeCachePort;
import com.ute.ticket.event.domain.entity.TicketType;
import com.ute.ticket.order.application.command.CreateOrderCommand;
import com.ute.ticket.order.application.port.in.CreateOrderUseCase;
import com.ute.ticket.order.application.port.out.OrderRepository;
import com.ute.ticket.order.application.result.OrderResult;
import com.ute.ticket.order.domain.entity.Order;
import com.ute.ticket.order.domain.entity.OrderItem;
import com.ute.ticket.reservation.application.port.out.CreateReservationPort;
import com.ute.ticket.shared.exception.BadRequestException;
import com.ute.ticket.shared.exception.NotFoundException;
import com.ute.ticket.shared.utils.UuidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service("createOrderLUA")
@RequiredArgsConstructor
public class CreateOrderLUAService implements CreateOrderUseCase {

    private final InventoryReservationPort inventoryReservationPort;
    private final TicketTypeCachePort ticketTypeCachePort;
    private final CreateReservationPort createReservationPort;
    private final InventoryReservePort inventoryDBReservePort;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResult execute(CreateOrderCommand cmd) {
        boolean cacheReserved = false;
        try {
            long cacheResult = inventoryReservationPort.decrease(
                    cmd.getSessionId(),
                    cmd.getTicketTypeId(),
                    cmd.getQuantity(),
                    Instant.now().getEpochSecond()
            );
            if (cacheResult == -1 || cacheResult == -3) {
                // reload/warm cache
            }
            if (cacheResult == 0) {
                throw new BadRequestException("Session is out of stock");
            }
            if (cacheResult == -4) {
                throw new BadRequestException("Session is not published yet");
            }
            if (cacheResult == -5) {
                throw new BadRequestException("Session is not currently on sale");
            }
            if (cacheResult != 1) {
                throw new BadRequestException("Unable to reserve inventory");
            }
            cacheReserved = true;
            TicketType ticketType = ticketTypeCachePort
                    .findActiveById(cmd.getTicketTypeId())
                    .orElseThrow(() -> new NotFoundException(
                            "Ticket type " + cmd.getTicketTypeId() + " not found"
                    ));
            boolean dbReserveSuccess = inventoryDBReservePort.reserve(
                    cmd.getTicketTypeId(),
                    cmd.getQuantity()
            );
            if (!dbReserveSuccess) {
                throw new BadRequestException("Insufficient inventory");
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
            if (cacheReserved) {
                inventoryReservationPort.release(
                        cmd.getTicketTypeId(),
                        cmd.getQuantity()
                );
            }
            throw e;
        }
    }
}
