package com.ute.ticket.venue.infrastructure.persistence.adapter;

import com.ute.ticket.shared.dto.PageInfo;
import com.ute.ticket.shared.exception.NotFoundException;
import com.ute.ticket.venue.application.port.out.VenueRepository;
import com.ute.ticket.venue.domain.entity.Venue;
import com.ute.ticket.venue.domain.enums.VenueStatus;
import com.ute.ticket.venue.infrastructure.persistence.jpa.entity.VenueJpaEntity;
import com.ute.ticket.venue.infrastructure.persistence.jpa.mapper.VenueMapper;
import com.ute.ticket.venue.infrastructure.persistence.jpa.repository.VenueJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VenueRepositoryAdapter implements VenueRepository {

    private final VenueJpaRepository venueJpaRepository;
    private final VenueMapper venueMapper;

    @Override
    public Venue save(Venue venue) {
        VenueJpaEntity jpaEntity;
        if (venue.getId() == null) {
            jpaEntity = venueMapper.toJpaEntity(venue);
        } else {
            jpaEntity = venueJpaRepository.findByIdAndDeletedAtIsNull(venue.getId())
                    .orElseThrow(() -> new NotFoundException("Venue not found"));
            venueMapper.updateEntity(jpaEntity, venue);
        }
        VenueJpaEntity saved = venueJpaRepository.save(jpaEntity);
        return venueMapper.toDomain(saved);
    }

    @Override
    public Optional<Venue> findById(Long id) {
        return venueJpaRepository.findByIdAndDeletedAtIsNull(id)
                .map(venueMapper::toDomain);
    }

    @Override
    public PageInfo<Venue> search(
            String keyword,
            String city,
            VenueStatus status,
            int page,
            int size,
            String sortBy,
            boolean ascending
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<VenueJpaEntity> venues = venueJpaRepository.search(keyword, city, status, pageRequest);

        return PageInfo.<Venue>builder()
                .pageContent(venues.getContent().stream().map(venueMapper::toDomain).toList())
                .number(venues.getNumber())
                .size(venues.getSize())
                .totalElements(venues.getTotalElements())
                .totalPages(venues.getTotalPages())
                .empty(venues.isEmpty())
                .numberOfElements(venues.getNumberOfElements())
                .hasNextPage(venues.hasNext())
                .hasPreviousPage(venues.hasPrevious())
                .build();
    }
}
