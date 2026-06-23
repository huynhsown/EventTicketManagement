package com.ute.ticket.event.application.port.in;

import com.ute.ticket.event.application.command.DeleteCategoryCommand;
import com.ute.ticket.event.application.result.CategoryResult;

public interface DeleteCategoryUseCase {
    CategoryResult execute(DeleteCategoryCommand cmd);
}
