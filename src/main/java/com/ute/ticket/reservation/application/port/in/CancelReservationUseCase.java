package com.ute.ticket.reservation.application.port.in;

import com.ute.ticket.reservation.application.command.CancelReservationCommand;
import com.ute.ticket.reservation.application.result.ReservationResult;

public interface CancelReservationUseCase {
    ReservationResult execute(CancelReservationCommand cmd);
}
