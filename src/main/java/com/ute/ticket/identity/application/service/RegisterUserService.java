package com.ute.ticket.identity.application.service;

import com.ute.ticket.identity.application.command.CreateUserCommand;
import com.ute.ticket.identity.application.port.in.RegisterUserUseCase;
import com.ute.ticket.identity.application.port.out.IdentityProvider;
import com.ute.ticket.identity.application.result.UserResult;
import com.ute.ticket.identity.domain.entity.User;
import com.ute.ticket.identity.application.port.out.UserRepository;
import com.ute.ticket.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final IdentityProvider identityProvider;

    @Override
    public UserResult register(CreateUserCommand cmd) {
        if (!cmd.getPassword().equals(cmd.getConfirmPassword())) {
            throw new BadRequestException("Password and confirm password do not match");
        }

        if (userRepository.existsByUsername(cmd.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        if (userRepository.existsByEmail(cmd.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        String authId = identityProvider.createUser(cmd);
        try {
            User user = new User(null, cmd.getUsername(),
                    authId, cmd.getEmail(), cmd.getFullName(),
                    cmd.getPhone(), cmd.getAvatarUrl());
            user = userRepository.save(user);
            return UserResult.from(user);
        }
        catch (Exception e) {
            identityProvider.deleteUser(authId);
            throw e;
        }
    }
}
