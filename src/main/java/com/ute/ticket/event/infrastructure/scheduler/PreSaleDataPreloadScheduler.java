package com.ute.ticket.event.infrastructure.scheduler;

import com.ute.ticket.event.application.port.in.PreSalePreloadUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PreSaleDataPreloadScheduler {
    private final PreSalePreloadUseCase preSalePreloadUseCase;

    @Scheduled(fixedDelay = 60_000)
    public void preSaleScheduler() {
        preSalePreloadUseCase.execute();
    }
}
