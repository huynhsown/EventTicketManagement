package com.ute.ticket.event.application.port.in;

import com.ute.ticket.event.application.command.ChangeEventCategoryCommand;
import com.ute.ticket.event.application.result.EventResult;

public interface ChangeEventCategoryUseCase {
    EventResult execute(ChangeEventCategoryCommand cmd);
}
