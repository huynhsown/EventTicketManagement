package com.ute.ticket.reservation.application.service;

import com.ute.ticket.reservation.application.command.ConfirmReservationCommand;
import com.ute.ticket.reservation.application.port.in.ConfirmReservationUseCase;
import com.ute.ticket.reservation.application.port.out.ReservationRepository;
import com.ute.ticket.reservation.application.result.ReservationResult;
import com.ute.ticket.reservation.domain.entity.Reservation;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConfirmReservationService implements ConfirmReservationUseCase {

    private final ReservationRepository reservationRepository;

    @Override
    public ReservationResult execute(ConfirmReservationCommand cmd) {
        Reservation reservation = reservationRepository.findById(cmd.getId())
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        reservation.confirm();
        reservation = reservationRepository.save(reservation);

        return ReservationResult.from(reservation);
    }
}
