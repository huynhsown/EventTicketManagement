package com.ute.ticket.event.application.port.in;

import com.ute.ticket.event.application.result.EventDetailResult;

public interface GetEventDetailUseCase {

    EventDetailResult execute(String slug);
}
