package com.ute.ticket.event.application.port.in;

import com.ute.ticket.event.application.command.UpdateCategoryCommand;
import com.ute.ticket.event.application.result.CategoryResult;

public interface UpdateCategoryUseCase {
    CategoryResult execute(UpdateCategoryCommand cmd);
}
