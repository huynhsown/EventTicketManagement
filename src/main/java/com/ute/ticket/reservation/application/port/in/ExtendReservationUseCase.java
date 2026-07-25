package com.ute.ticket.reservation.application.port.in;

import com.ute.ticket.reservation.application.command.ExtendReservationCommand;
import com.ute.ticket.reservation.application.result.ReservationResult;

public interface ExtendReservationUseCase {
    ReservationResult execute(ExtendReservationCommand cmd);
}
