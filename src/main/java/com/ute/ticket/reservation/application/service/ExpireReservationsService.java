package com.ute.ticket.reservation.application.service;

import com.ute.ticket.reservation.application.port.in.ExpireReservationsUseCase;
import com.ute.ticket.reservation.application.port.out.ReservationRepository;
import com.ute.ticket.reservation.domain.entity.Reservation;
import com.ute.ticket.reservation.domain.enums.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpireReservationsService implements ExpireReservationsUseCase {

    private final ReservationRepository reservationRepository;

    @Override
    public int execute() {
        List<Reservation> expired = reservationRepository.findAllByStatusAndExpiresAtBefore(
                ReservationStatus.PENDING,
                Instant.now()
        );

        for (Reservation reservation : expired) {
            reservation.expire();
            reservationRepository.save(reservation);
        }

        return expired.size();
    }
}
