package com.ute.ticket.reservation.infrastructure.persistence.adapter;

import com.ute.ticket.reservation.application.port.out.ReservationRepository;
import com.ute.ticket.reservation.domain.entity.Reservation;
import com.ute.ticket.reservation.domain.enums.ReservationStatus;
import com.ute.ticket.reservation.infrastructure.persistence.jpa.entity.ReservationJpaEntity;
import com.ute.ticket.reservation.infrastructure.persistence.jpa.mapper.ReservationMapper;
import com.ute.ticket.reservation.infrastructure.persistence.jpa.repository.ReservationJpaRepository;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryAdapter implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public Reservation save(Reservation reservation) {
        ReservationJpaEntity jpaEntity;
        if (reservation.getId() == null || reservation.getVersion() == null) {
            jpaEntity = reservationMapper.toJpaEntity(reservation);
        } else {
            jpaEntity = reservationJpaRepository.findById(reservation.getId())
                    .orElseThrow(() -> new NotFoundException("Reservation not found"));
            reservationMapper.updateEntity(jpaEntity, reservation);
        }
        ReservationJpaEntity saved = reservationJpaRepository.save(jpaEntity);
        return reservationMapper.toDomain(saved);
    }

    @Override
    public Optional<Reservation> findById(UUID id) {
        return reservationJpaRepository.findById(id)
                .map(reservationMapper::toDomain);
    }

    @Override
    public boolean existsById(UUID id) {
        return reservationJpaRepository.existsById(id);
    }

    @Override
    public List<Reservation> findAllByStatusAndExpiresAtBefore(ReservationStatus status, Instant expiresAt) {
        return reservationJpaRepository.findAllByStatusAndExpiresAtBefore(status, expiresAt)
                .stream()
                .map(reservationMapper::toDomain)
                .toList();
    }
}
