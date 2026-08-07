package com.ute.ticket.event.application.port.out;

public interface SessionEligibilityPort {

    boolean isEligibleForPurchase(Long sessionId, Long ticketTypeId, int quantity);
}
