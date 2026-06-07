package com.ute.ticket.identity.infrastructure.persistence.adapter;

import com.ute.ticket.identity.domain.entity.User;
import com.ute.ticket.identity.application.port.out.UserRepository;
import com.ute.ticket.identity.infrastructure.persistence.jpa.entity.UserJpaEntity;
import com.ute.ticket.identity.infrastructure.persistence.jpa.mapper.UserMapper;
import com.ute.ticket.identity.infrastructure.persistence.jpa.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        UserJpaEntity jpaEntity;
        if (user.getId() == null) {
            jpaEntity = userMapper.toJpaEntity(user);
        } else {
            jpaEntity = userJpaRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            userMapper.updateEntity(jpaEntity, user);
        }
        UserJpaEntity saved = userJpaRepository.save(jpaEntity);
        return userMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByAuthId(String authId) {
        return userJpaRepository.findByAuthId(authId)
                .map(userMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
