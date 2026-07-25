package com.ute.ticket.reservation.presentation;

import com.ute.ticket.reservation.application.facade.ReservationFacade;
import com.ute.ticket.reservation.application.result.ReservationResult;
import com.ute.ticket.reservation.presentation.dto.ChangeReservationQuantityRequest;
import com.ute.ticket.reservation.presentation.dto.CreateReservationRequest;
import com.ute.ticket.reservation.presentation.dto.ExtendReservationRequest;
import com.ute.ticket.reservation.presentation.mapper.ReservationPresentationMapper;
import com.ute.ticket.shared.application.security.CurrentUser;
import com.ute.ticket.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservation", description = "Reservation endpoints")
public class ReservationController {

    private final ReservationFacade reservationFacade;
    private final ReservationPresentationMapper reservationPresentationMapper;
    private final CurrentUser currentUser;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new reservation")
    public ApiResponse<ReservationResult> create(@Valid @RequestBody CreateReservationRequest request) {
        var command = reservationPresentationMapper.toCommand(request, currentUser.getUserId());
        var result = reservationFacade.createReservation(command);
        return ApiResponse.<ReservationResult>builder()
                .success(true)
                .message("Reservation created successfully")
                .data(result)
                .build();
    }

    @PatchMapping("/{id}/confirm")
    @Operation(summary = "Confirm a reservation")
    public ApiResponse<ReservationResult> confirm(@PathVariable UUID id) {
        var result = reservationFacade.confirmReservation(id);
        return ApiResponse.<ReservationResult>builder()
                .success(true)
                .message("Reservation confirmed successfully")
                .data(result)
                .build();
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel a reservation (owner)")
    public ApiResponse<ReservationResult> cancel(@PathVariable UUID id) {
        var result = reservationFacade.cancelReservation(id, currentUser.getUserId());
        return ApiResponse.<ReservationResult>builder()
                .success(true)
                .message("Reservation cancelled successfully")
                .data(result)
                .build();
    }

    @PatchMapping("/{id}/quantity")
    @Operation(summary = "Change reservation quantity (owner)")
    public ApiResponse<ReservationResult> changeQuantity(
            @PathVariable UUID id,
            @Valid @RequestBody ChangeReservationQuantityRequest request
    ) {
        var command = reservationPresentationMapper.toCommand(id, request, currentUser.getUserId());
        var result = reservationFacade.changeReservationQuantity(command);
        return ApiResponse.<ReservationResult>builder()
                .success(true)
                .message("Reservation quantity changed successfully")
                .data(result)
                .build();
    }

    @PatchMapping("/{id}/extend")
    @Operation(summary = "Extend reservation timeout (owner)")
    public ApiResponse<ReservationResult> extend(
            @PathVariable UUID id,
            @Valid @RequestBody ExtendReservationRequest request
    ) {
        var command = reservationPresentationMapper.toCommand(id, request, currentUser.getUserId());
        var result = reservationFacade.extendReservation(command);
        return ApiResponse.<ReservationResult>builder()
                .success(true)
                .message("Reservation extended successfully")
                .data(result)
                .build();
    }
}
