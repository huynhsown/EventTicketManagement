package com.ute.ticket.reservation.infrastructure.scheduler;

import com.ute.ticket.reservation.application.port.in.ExpireReservationsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationExpirationScheduler {

    private final ExpireReservationsUseCase expireReservationsUseCase;

    @Scheduled(fixedDelay = 60_000)
    public void expireReservations() {
        int expiredCount = expireReservationsUseCase.execute();
        if (expiredCount > 0) {
            log.info("Expired {} reservation(s)", expiredCount);
        }
    }
}
