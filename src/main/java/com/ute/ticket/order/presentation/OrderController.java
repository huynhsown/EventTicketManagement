package com.ute.ticket.order.presentation;

import com.ute.ticket.order.application.facade.OrderFacade;
import com.ute.ticket.order.application.result.OrderResult;
import com.ute.ticket.order.presentation.dto.CreateOrderRequest;
import com.ute.ticket.order.presentation.mapper.OrderPresentationMapper;
import com.ute.ticket.shared.application.security.CurrentUser;
import com.ute.ticket.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order endpoints")
public class OrderController {

    private final OrderFacade orderFacade;
    private final OrderPresentationMapper orderPresentationMapper;
    private final CurrentUser currentUser;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new order")
    public ApiResponse<OrderResult> create(@Valid @RequestBody CreateOrderRequest request) {
        var command = orderPresentationMapper.toCommand(request, currentUser.getUserId());
        var result = orderFacade.createOrder(command);
        return ApiResponse.<OrderResult>builder()
                .success(true)
                .message("Order created successfully")
                .data(result)
                .build();
    }
}