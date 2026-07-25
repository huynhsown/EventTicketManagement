package com.ute.ticket.reservation.application.port.in;

import com.ute.ticket.reservation.application.command.ConfirmReservationCommand;
import com.ute.ticket.reservation.application.result.ReservationResult;

public interface ConfirmReservationUseCase {
    ReservationResult execute(ConfirmReservationCommand cmd);
}
