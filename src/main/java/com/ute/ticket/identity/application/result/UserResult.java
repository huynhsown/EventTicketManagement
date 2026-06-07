package com.ute.ticket.identity.application.result;

import com.ute.ticket.identity.domain.entity.User;

public record UserResult(
        Long id,
        String username,
        String email,
        String fullName,
        String phone,
        String avatarUrl
) {
    public static UserResult from(User user) {
        return new UserResult(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getPhone(),
                user.getAvatarUrl()
        );
    }
}
