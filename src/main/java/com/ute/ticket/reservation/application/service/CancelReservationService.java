package com.ute.ticket.reservation.application.service;

import com.ute.ticket.reservation.application.command.CancelReservationCommand;
import com.ute.ticket.reservation.application.port.in.CancelReservationUseCase;
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
public class CancelReservationService implements CancelReservationUseCase {

    private final ReservationRepository reservationRepository;

    @Override
    public ReservationResult execute(CancelReservationCommand cmd) {
        Reservation reservation = reservationRepository.findById(cmd.getId())
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        reservation.ensureOwnedBy(cmd.getUserId());

        reservation.cancel();
        reservation = reservationRepository.save(reservation);

        return ReservationResult.from(reservation);
    }
}
