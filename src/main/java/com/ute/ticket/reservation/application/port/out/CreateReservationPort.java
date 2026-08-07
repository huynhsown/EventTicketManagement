package com.ute.ticket.reservation.application.port.out;

import com.ute.ticket.reservation.application.result.ReservationResult;

public interface CreateReservationPort {

    ReservationResult create(Long userId, Long ticketTypeId, int quantity);
}