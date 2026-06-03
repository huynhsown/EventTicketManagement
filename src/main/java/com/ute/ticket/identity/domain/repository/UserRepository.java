package com.ute.ticket.identity.domain.repository;

import com.ute.ticket.identity.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByAuthId(String authId);
    boolean existsByEmail(String email);
}
