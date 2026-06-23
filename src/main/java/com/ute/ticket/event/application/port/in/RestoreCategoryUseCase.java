package com.ute.ticket.event.application.port.in;

import com.ute.ticket.event.application.command.RestoreCategoryCommand;
import com.ute.ticket.event.application.result.CategoryResult;

public interface RestoreCategoryUseCase {
    CategoryResult execute(RestoreCategoryCommand cmd);
}
