package com.ute.ticket.reservation.application.service;

import com.ute.ticket.reservation.application.command.CreateReservationCommand;
import com.ute.ticket.reservation.application.port.in.CreateReservationUseCase;
import com.ute.ticket.reservation.application.port.out.CreateReservationPort;
import com.ute.ticket.reservation.application.port.out.ReservationRepository;
import com.ute.ticket.reservation.application.result.ReservationResult;
import com.ute.ticket.reservation.domain.entity.Reservation;
import com.ute.ticket.shared.utils.UuidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateReservationService implements CreateReservationUseCase, CreateReservationPort {

    private static final long RESERVATION_TTL_MINUTES = 15;

    private final ReservationRepository reservationRepository;

    @Override
    public ReservationResult execute(CreateReservationCommand cmd) {
        return create(cmd.getUserId(), cmd.getTicketTypeId(), cmd.getQuantity());
    }

    @Override
    public ReservationResult create(Long userId, Long ticketTypeId, int quantity) {
        Reservation reservation = Reservation.create(
                UuidGenerator.v7(),
                userId,
                ticketTypeId,
                quantity,
                Instant.now().plus(RESERVATION_TTL_MINUTES, ChronoUnit.MINUTES)
        );

        reservation = reservationRepository.save(reservation);

        return ReservationResult.from(reservation);
    }
}
