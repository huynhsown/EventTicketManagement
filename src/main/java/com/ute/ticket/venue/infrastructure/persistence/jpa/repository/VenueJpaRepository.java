package com.ute.ticket.venue.infrastructure.persistence.jpa.repository;

import com.ute.ticket.venue.domain.enums.VenueStatus;
import com.ute.ticket.venue.infrastructure.persistence.jpa.entity.VenueJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VenueJpaRepository extends JpaRepository<VenueJpaEntity, Long> {

    Optional<VenueJpaEntity> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByIdAndDeletedAtIsNullAndStatus(Long id, VenueStatus status);

    @Query("""
            SELECT venue
            FROM VenueJpaEntity venue
            WHERE venue.deletedAt IS NULL
              AND (:keyword IS NULL OR :keyword = ''
                   OR LOWER(venue.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:city IS NULL OR :city = ''
                   OR LOWER(venue.city) = LOWER(:city))
              AND (:status IS NULL OR venue.status = :status)
            """)
    Page<VenueJpaEntity> search(
            @Param("keyword") String keyword,
            @Param("city") String city,
            @Param("status") VenueStatus status,
            Pageable pageable
    );
}
