package com.ute.ticket.event.application.port.in;

import com.ute.ticket.event.application.command.RenameCategoryCommand;
import com.ute.ticket.event.application.result.CategoryResult;

public interface RenameCategoryUseCase {
    CategoryResult execute(RenameCategoryCommand cmd);
}
