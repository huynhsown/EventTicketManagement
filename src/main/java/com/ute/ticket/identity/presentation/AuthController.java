package com.ute.ticket.identity.presentation;

import com.ute.ticket.identity.application.port.in.LoginUserUseCase;
import com.ute.ticket.identity.application.port.in.LogoutUseCase;
import com.ute.ticket.identity.application.port.in.RefreshTokenUseCase;
import com.ute.ticket.identity.application.port.in.RegisterUserUseCase;
import com.ute.ticket.identity.application.result.LoginResult;
import com.ute.ticket.identity.application.result.UserResult;
import com.ute.ticket.identity.presentation.dto.LoginRequest;
import com.ute.ticket.identity.presentation.dto.LogoutRequest;
import com.ute.ticket.identity.presentation.dto.RefreshTokenRequest;
import com.ute.ticket.identity.presentation.dto.RegisterRequest;
import com.ute.ticket.identity.presentation.mapper.LoginMapper;
import com.ute.ticket.identity.presentation.mapper.LogoutMapper;
import com.ute.ticket.identity.presentation.mapper.RefreshTokenMapper;
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
    private final LoginUserUseCase loginUserUseCase;
    private final LoginMapper loginMapper;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final RefreshTokenMapper refreshTokenMapper;
    private final LogoutUseCase logoutUseCase;
    private final LogoutMapper logoutMapper;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new user")
    public ApiResponse<UserResult> register(@Valid @RequestBody RegisterRequest request) {
        var command = registerMapper.toCommand(request);
        var result = registerUserUseCase.register(command);
        return ApiResponse.<UserResult>builder()
                .success(true)
                .message("User registered successfully")
                .data(result)
                .build();
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and return tokens")
    public ApiResponse<LoginResult> login(@Valid @RequestBody LoginRequest request) {
        var command = loginMapper.toCommand(request);
        var result = loginUserUseCase.login(command);
        return ApiResponse.<LoginResult>builder()
                .success(true)
                .message("Login successful")
                .data(result)
                .build();
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token")
    public ApiResponse<LoginResult> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        var command = refreshTokenMapper.toCommand(request);
        var result = refreshTokenUseCase.refreshToken(command);
        return ApiResponse.<LoginResult>builder()
                .success(true)
                .message("Token refreshed successfully")
                .data(result)
                .build();
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout and invalidate refresh token")
    public ApiResponse<Void> logout(@Valid @RequestBody LogoutRequest request) {
        var command = logoutMapper.toCommand(request);
        logoutUseCase.logout(command);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Logged out successfully")
                .build();
    }
}
