package com.ute.ticket.identity.infrastructure.persistence.jpa.repository;

import com.ute.ticket.identity.infrastructure.persistence.jpa.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    Optional<UserJpaEntity> findByEmail(String email);
    Optional<UserJpaEntity> findByAuthId(String authId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
