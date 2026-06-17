package com.ute.ticket.venue.infrastructure.persistence.jpa.mapper;

import com.ute.ticket.venue.domain.entity.Venue;
import com.ute.ticket.venue.infrastructure.persistence.jpa.entity.VenueJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class VenueMapper {

    public Venue toDomain(VenueJpaEntity entity) {
        return Venue.restore(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getCity(),
                entity.getCountry(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getCapacity(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedBy(),
                entity.getUpdatedBy(),
                entity.getDeletedAt(),
                entity.getVersion()
        );
    }

    public VenueJpaEntity toJpaEntity(Venue domain) {
        return VenueJpaEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .address(domain.getAddress())
                .city(domain.getCity())
                .country(domain.getCountry())
                .latitude(domain.getLatitude())
                .longitude(domain.getLongitude())
                .capacity(domain.getCapacity())
                .description(domain.getDescription())
                .status(domain.getStatus())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedBy(domain.getUpdatedBy())
                .deletedAt(domain.getDeletedAt())
                .version(domain.getVersion())
                .build();
    }

    public void updateEntity(VenueJpaEntity entity, Venue domain) {
        entity.setName(domain.getName());
        entity.setAddress(domain.getAddress());
        entity.setCity(domain.getCity());
        entity.setCountry(domain.getCountry());
        entity.setLatitude(domain.getLatitude());
        entity.setLongitude(domain.getLongitude());
        entity.setCapacity(domain.getCapacity());
        entity.setDescription(domain.getDescription());
        entity.setStatus(domain.getStatus());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setVersion(domain.getVersion());
    }
}
