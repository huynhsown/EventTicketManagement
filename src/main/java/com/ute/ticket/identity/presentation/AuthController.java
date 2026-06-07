package com.ute.ticket.identity.presentation;

import com.ute.ticket.identity.application.port.in.RegisterUserUseCase;
import com.ute.ticket.identity.application.result.UserResult;
import com.ute.ticket.identity.presentation.dto.RegisterRequest;
import com.ute.ticket.identity.presentation.mapper.RegisterMapper;
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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication endpoints")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final RegisterMapper registerMapper;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResult> register(@Valid @RequestBody RegisterRequest request) {
        var command = registerMapper.toCommand(request);
        var result = registerUserUseCase.register(command);
        return ApiResponse.<UserResult>builder()
                .success(true)
                .message("User registered successfully")
                .data(result)
                .build();
    }
}
