package com.ute.ticket.identity.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class User {

    private final Long id;
    private String username;
    private String authId;
    private String email;
    private String fullName;
    private String phone;
    private String avatarUrl;

    public void updateProfile(String fullName,
                              String phone,
                              String avatarUrl) {
        this.fullName = fullName;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
    }

    public boolean hasLinkedAuth() {
        return authId != null && !authId.isBlank();
    }

    public boolean isSameUser(Long userId) {
        return id.equals(userId);
    }
}
