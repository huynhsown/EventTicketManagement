package com.ute.ticket.event.infrastructure.persistence.jpa.mapper;

import com.ute.ticket.event.domain.entity.Event;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.EventJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public Event toDomain(EventJpaEntity entity) {
        return Event.builder()
                .id(entity.getId())
                .organizationId(entity.getOrganizationId())
                .venueId(entity.getVenueId())
                .title(entity.getTitle())
                .slug(entity.getSlug())
                .description(entity.getDescription())
                .bannerUrl(entity.getBannerUrl())
                .status(entity.getStatus())
                .publishedAt(entity.getPublishedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .deletedAt(entity.getDeletedAt())
                .version(entity.getVersion())
                .build();
    }

    public EventJpaEntity toJpaEntity(Event domain) {
        return EventJpaEntity.builder()
                .id(domain.getId())
                .organizationId(domain.getOrganizationId())
                .venueId(domain.getVenueId())
                .title(domain.getTitle())
                .slug(domain.getSlug())
                .description(domain.getDescription())
                .bannerUrl(domain.getBannerUrl())
                .status(domain.getStatus())
                .publishedAt(domain.getPublishedAt())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedBy(domain.getUpdatedBy())
                .deletedAt(domain.getDeletedAt())
                .version(domain.getVersion())
                .build();
    }

    public void updateEntity(EventJpaEntity entity, Event domain) {
        entity.setVenueId(domain.getVenueId());
        entity.setTitle(domain.getTitle());
        entity.setSlug(domain.getSlug());
        entity.setDescription(domain.getDescription());
        entity.setBannerUrl(domain.getBannerUrl());
        entity.setStatus(domain.getStatus());
        entity.setPublishedAt(domain.getPublishedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setDeletedAt(domain.getDeletedAt());
        entity.setVersion(domain.getVersion());
    }
}
