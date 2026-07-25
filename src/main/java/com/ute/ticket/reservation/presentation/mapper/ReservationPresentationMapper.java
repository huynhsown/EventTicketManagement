package com.ute.ticket.reservation.presentation.mapper;

import com.ute.ticket.reservation.application.command.ChangeReservationQuantityCommand;
import com.ute.ticket.reservation.application.command.CreateReservationCommand;
import com.ute.ticket.reservation.application.command.ExtendReservationCommand;
import com.ute.ticket.reservation.presentation.dto.ChangeReservationQuantityRequest;
import com.ute.ticket.reservation.presentation.dto.CreateReservationRequest;
import com.ute.ticket.reservation.presentation.dto.ExtendReservationRequest;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
public class ReservationPresentationMapper {

    public CreateReservationCommand toCommand(CreateReservationRequest request, Long userId) {
        return CreateReservationCommand.builder()
                .userId(userId)
                .ticketTypeId(request.getTicketTypeId())
                .quantity(request.getQuantity())
                .build();
    }

    public ChangeReservationQuantityCommand toCommand(
            UUID id,
            ChangeReservationQuantityRequest request,
            Long userId
    ) {
        return ChangeReservationQuantityCommand.builder()
                .id(id)
                .userId(userId)
                .quantity(request.getQuantity())
                .build();
    }

    public ExtendReservationCommand toCommand(
            UUID id,
            ExtendReservationRequest request,
            Long userId
    ) {
        return ExtendReservationCommand.builder()
                .id(id)
                .userId(userId)
                .extension(Duration.ofMinutes(request.getMinutes()))
                .build();
    }
}
