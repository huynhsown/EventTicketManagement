package com.ute.ticket.event.application.port.in;

import com.ute.ticket.event.application.command.CreateCategoryCommand;
import com.ute.ticket.event.application.result.CategoryResult;

public interface CreateCategoryUseCase {
    CategoryResult execute(CreateCategoryCommand cmd);
}
