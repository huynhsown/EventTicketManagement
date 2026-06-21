package com.ute.ticket.venue.domain.entity;

import com.ute.ticket.shared.domain.BaseDomain;
import com.ute.ticket.shared.exception.DomainValidationException;
import com.ute.ticket.venue.domain.enums.VenueStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@SuperBuilder
@AllArgsConstructor
public class Venue extends BaseDomain {

    private static final int NAME_MAX_LENGTH = 255;
    private static final int CITY_MAX_LENGTH = 100;
    private static final int COUNTRY_MAX_LENGTH = 100;
    private static final BigDecimal MIN_LATITUDE = BigDecimal.valueOf(-90);
    private static final BigDecimal MAX_LATITUDE = BigDecimal.valueOf(90);
    private static final BigDecimal MIN_LONGITUDE = BigDecimal.valueOf(-180);
    private static final BigDecimal MAX_LONGITUDE = BigDecimal.valueOf(180);

    private final Long id;
    private String name;
    private String address;
    private String city;
    private String country;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer capacity;
    private String description;
    private VenueStatus status;

    public static Venue create(
            String name,
            String address,
            String city,
            String country,
            BigDecimal latitude,
            BigDecimal longitude,
            Integer capacity,
            String description,
            VenueStatus status
    ) {
        return Venue.builder()
                .name(validateText(name, "Venue name cannot be blank.", NAME_MAX_LENGTH, "Venue name is too long."))
                .address(validateRequiredText(address, "Venue address cannot be blank."))
                .city(validateText(city, "Venue city cannot be blank.", CITY_MAX_LENGTH, "Venue city is too long."))
                .country(validateText(country, "Venue country cannot be blank.", COUNTRY_MAX_LENGTH, "Venue country is too long."))
                .latitude(validateLatitude(latitude))
                .longitude(validateLongitude(longitude))
                .capacity(validateCapacity(capacity))
                .description(description)
                .status(validateStatus(status))
                .build();
    }

    public static Venue restore(
            Long id,
            String name,
            String address,
            String city,
            String country,
            BigDecimal latitude,
            BigDecimal longitude,
            Integer capacity,
            String description,
            VenueStatus status,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        return Venue.builder()
                .id(id)
                .name(validateText(name, "Venue name cannot be blank.", NAME_MAX_LENGTH, "Venue name is too long."))
                .address(validateRequiredText(address, "Venue address cannot be blank."))
                .city(validateText(city, "Venue city cannot be blank.", CITY_MAX_LENGTH, "Venue city is too long."))
                .country(validateText(country, "Venue country cannot be blank.", COUNTRY_MAX_LENGTH, "Venue country is too long."))
                .latitude(validateLatitude(latitude))
                .longitude(validateLongitude(longitude))
                .capacity(validateCapacity(capacity))
                .description(description)
                .status(validateStatus(status))
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .deletedAt(deletedAt)
                .version(version)
                .build();
    }

    public void update(
            String name,
            String address,
            String city,
            String country,
            BigDecimal latitude,
            BigDecimal longitude,
            Integer capacity,
            String description
    ) {
        this.name = validateText(name, "Venue name cannot be blank.", NAME_MAX_LENGTH, "Venue name is too long.");
        this.address = validateRequiredText(address, "Venue address cannot be blank.");
        this.city = validateText(city, "Venue city cannot be blank.", CITY_MAX_LENGTH, "Venue city is too long.");
        this.country = validateText(country, "Venue country cannot be blank.", COUNTRY_MAX_LENGTH, "Venue country is too long.");
        this.latitude = validateLatitude(latitude);
        this.longitude = validateLongitude(longitude);
        this.capacity = validateCapacity(capacity);
        this.description = description;
    }

    public void changeStatus(VenueStatus status) {
        this.status = validateStatus(status);
    }

    public void delete() {
        markDeleted();
    }

    private static String validateText(String value, String blankMessage, int maxLength, String lengthMessage) {
        String normalized = validateRequiredText(value, blankMessage);

        if (normalized.length() > maxLength) {
            throw new DomainValidationException(lengthMessage);
        }

        return normalized;
    }

    private static String validateRequiredText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException(message);
        }

        return value.trim();
    }

    private static Integer validateCapacity(Integer capacity) {
        if (capacity == null || capacity <= 0) {
            throw new DomainValidationException("Venue capacity must be greater than zero.");
        }

        return capacity;
    }

    private static BigDecimal validateLatitude(BigDecimal latitude) {
        if (latitude != null
                && (latitude.compareTo(MIN_LATITUDE) < 0 || latitude.compareTo(MAX_LATITUDE) > 0)) {
            throw new DomainValidationException("Venue latitude must be between -90 and 90.");
        }

        return latitude;
    }

    private static BigDecimal validateLongitude(BigDecimal longitude) {
        if (longitude != null
                && (longitude.compareTo(MIN_LONGITUDE) < 0 || longitude.compareTo(MAX_LONGITUDE) > 0)) {
            throw new DomainValidationException("Venue longitude must be between -180 and 180.");
        }

        return longitude;
    }

    private static VenueStatus validateStatus(VenueStatus status) {
        if (status == null) {
            throw new DomainValidationException("Venue status cannot be null.");
        }

        return status;
    }
}
