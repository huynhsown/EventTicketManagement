package com.ute.ticket.identity.domain.entity;

import com.ute.ticket.shared.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class User extends BaseDomain {

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
