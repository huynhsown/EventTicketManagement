package com.ute.ticket.reservation.application.port.in;

import com.ute.ticket.reservation.application.command.CreateReservationCommand;
import com.ute.ticket.reservation.application.result.ReservationResult;

public interface CreateReservationUseCase {
    ReservationResult execute(CreateReservationCommand cmd);
}
