package com.ute.ticket.event.application.port.in;

import com.ute.ticket.event.application.command.InitializeInventoryCommand;
import com.ute.ticket.event.application.result.InventoryResult;

public interface InitializeInventoryUseCase {
    InventoryResult execute(InitializeInventoryCommand cmd);
}
