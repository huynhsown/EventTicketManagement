package com.ute.ticket.event.presentation;

import com.ute.ticket.event.application.facade.InventoryFacade;
import com.ute.ticket.event.application.result.InventoryResult;
import com.ute.ticket.event.presentation.dto.InitializeInventoryRequest;
import com.ute.ticket.event.presentation.mapper.InitializeInventoryMapper;
import com.ute.ticket.shared.application.security.CurrentUser;
import com.ute.ticket.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ticket-types/{ticketTypeId}/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Inventory endpoints")
public class InventoryController {

    private final InventoryFacade inventoryFacade;
    private final InitializeInventoryMapper initializeInventoryMapper;
    private final CurrentUser currentUser;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Initialize inventory for a ticket type (owner / admin)")
    public ApiResponse<InventoryResult> initialize(
            @PathVariable Long ticketTypeId,
            @Valid @RequestBody InitializeInventoryRequest request
    ) {
        var command = initializeInventoryMapper.toCommand(ticketTypeId, currentUser.getUserId(), request);
        var result = inventoryFacade.initializeInventory(command);
        return ApiResponse.<InventoryResult>builder()
                .success(true)
                .message("Inventory initialized successfully")
                .data(result)
                .build();
    }
}
