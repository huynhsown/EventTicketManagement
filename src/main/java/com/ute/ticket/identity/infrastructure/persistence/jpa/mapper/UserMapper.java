package com.ute.ticket.identity.infrastructure.persistence.jpa.mapper;

import com.ute.ticket.identity.domain.entity.User;
import com.ute.ticket.identity.infrastructure.persistence.jpa.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserJpaEntity entity) {
        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .authId(entity.getAuthId())
                .email(entity.getEmail())
                .fullName(entity.getFullName())
                .phone(entity.getPhone())
                .avatarUrl(entity.getAvatarUrl())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .version(entity.getVersion())
                .build();
    }

    public UserJpaEntity toJpaEntity(User domain) {
        return UserJpaEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .authId(domain.getAuthId())
                .email(domain.getEmail())
                .fullName(domain.getFullName())
                .phone(domain.getPhone())
                .avatarUrl(domain.getAvatarUrl())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedBy(domain.getUpdatedBy())
                .deletedAt(domain.getDeletedAt())
                .version(domain.getVersion())
                .build();
    }

    public void updateEntity(UserJpaEntity entity, User domain) {
        entity.setUsername(domain.getUsername());
        entity.setAuthId(domain.getAuthId());
        entity.setEmail(domain.getEmail());
        entity.setFullName(domain.getFullName());
        entity.setPhone(domain.getPhone());
        entity.setAvatarUrl(domain.getAvatarUrl());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setVersion(domain.getVersion());
    }
}