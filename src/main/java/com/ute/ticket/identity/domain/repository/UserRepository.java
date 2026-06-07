package com.ute.ticket.identity.domain.repository;

import com.ute.ticket.identity.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByAuthId(String authId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
