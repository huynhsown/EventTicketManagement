package com.ute.ticket.event.application.facade;

import com.ute.ticket.event.application.command.InitializeInventoryCommand;
import com.ute.ticket.event.application.port.in.InitializeInventoryUseCase;
import com.ute.ticket.event.application.result.InventoryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryFacade {

    private final InitializeInventoryUseCase initializeInventoryUseCase;

    public InventoryResult initializeInventory(InitializeInventoryCommand cmd) {
        return initializeInventoryUseCase.execute(cmd);
    }
}
