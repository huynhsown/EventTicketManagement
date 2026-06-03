package com.ute.ticket.identity.infrastructure.persistence.jpa.mapper;

import com.ute.ticket.identity.domain.entity.User;
import com.ute.ticket.identity.infrastructure.persistence.jpa.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserJpaEntity entity) {
        return User.builder()
                .id(entity.getId())
                .authId(entity.getAuthId())
                .email(entity.getEmail())
                .fullName(entity.getFullName())
                .phone(entity.getPhone())
                .avatarUrl(entity.getAvatarUrl())
                .build();
    }

    public UserJpaEntity toJpaEntity(User domain) {
        return UserJpaEntity.builder()
                .id(domain.getId())
                .authId(domain.getAuthId())
                .email(domain.getEmail())
                .fullName(domain.getFullName())
                .phone(domain.getPhone())
                .avatarUrl(domain.getAvatarUrl())
                .build();
    }

    public void updateEntity(UserJpaEntity entity, User domain) {
        entity.setAuthId(domain.getAuthId());
        entity.setEmail(domain.getEmail());
        entity.setFullName(domain.getFullName());
        entity.setPhone(domain.getPhone());
        entity.setAvatarUrl(domain.getAvatarUrl());
    }
}