package com.ute.ticket.event.application.port.in;

import com.ute.ticket.event.application.command.ArchiveCategoryCommand;
import com.ute.ticket.event.application.result.CategoryResult;

public interface ArchiveCategoryUseCase {
    CategoryResult execute(ArchiveCategoryCommand cmd);
}
