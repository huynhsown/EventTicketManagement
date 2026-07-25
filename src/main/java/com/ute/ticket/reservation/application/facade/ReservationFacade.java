package com.ute.ticket.reservation.application.facade;

import com.ute.ticket.reservation.application.command.CancelReservationCommand;
import com.ute.ticket.reservation.application.command.ChangeReservationQuantityCommand;
import com.ute.ticket.reservation.application.command.ConfirmReservationCommand;
import com.ute.ticket.reservation.application.command.CreateReservationCommand;
import com.ute.ticket.reservation.application.command.ExtendReservationCommand;
import com.ute.ticket.reservation.application.port.in.CancelReservationUseCase;
import com.ute.ticket.reservation.application.port.in.ChangeReservationQuantityUseCase;
import com.ute.ticket.reservation.application.port.in.ConfirmReservationUseCase;
import com.ute.ticket.reservation.application.port.in.CreateReservationUseCase;
import com.ute.ticket.reservation.application.port.in.ExtendReservationUseCase;
import com.ute.ticket.reservation.application.result.ReservationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationFacade {

    private final CreateReservationUseCase createReservationUseCase;
    private final ConfirmReservationUseCase confirmReservationUseCase;
    private final CancelReservationUseCase cancelReservationUseCase;
    private final ChangeReservationQuantityUseCase changeReservationQuantityUseCase;
    private final ExtendReservationUseCase extendReservationUseCase;

    public ReservationResult createReservation(CreateReservationCommand command) {
        return createReservationUseCase.execute(command);
    }

    public ReservationResult confirmReservation(UUID id) {
        var command = ConfirmReservationCommand.builder()
                .id(id)
                .build();
        return confirmReservationUseCase.execute(command);
    }

    public ReservationResult cancelReservation(UUID id, Long userId) {
        var command = CancelReservationCommand.builder()
                .id(id)
                .userId(userId)
                .build();
        return cancelReservationUseCase.execute(command);
    }

    public ReservationResult changeReservationQuantity(ChangeReservationQuantityCommand command) {
        return changeReservationQuantityUseCase.execute(command);
    }

    public ReservationResult extendReservation(ExtendReservationCommand command) {
        return extendReservationUseCase.execute(command);
    }
}
