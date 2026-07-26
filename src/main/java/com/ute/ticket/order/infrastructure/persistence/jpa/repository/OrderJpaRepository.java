package com.ute.ticket.order.infrastructure.persistence.jpa.repository;

import com.ute.ticket.order.infrastructure.persistence.jpa.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> {
    @EntityGraph(attributePaths = {
            "items"
    })
    Optional<OrderJpaEntity> findWithItemsById(Long id);
}
