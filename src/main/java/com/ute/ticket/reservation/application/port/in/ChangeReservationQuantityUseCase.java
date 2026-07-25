package com.ute.ticket.reservation.application.port.in;

import com.ute.ticket.reservation.application.command.ChangeReservationQuantityCommand;
import com.ute.ticket.reservation.application.result.ReservationResult;

public interface ChangeReservationQuantityUseCase {
    ReservationResult execute(ChangeReservationQuantityCommand cmd);
}
