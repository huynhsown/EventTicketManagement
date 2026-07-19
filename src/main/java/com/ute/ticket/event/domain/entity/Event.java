package com.ute.ticket.event.domain.entity;

import com.ute.ticket.event.domain.enums.EventStatus;
import com.ute.ticket.shared.domain.BaseDomain;
import com.ute.ticket.shared.exception.DomainConflictException;
import com.ute.ticket.shared.exception.DomainValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

@Getter
@SuperBuilder
@AllArgsConstructor
public class Event extends BaseDomain {

    private static final int MAX_TITLE_LENGTH = 255;
    private static final int MAX_DESCRIPTION_LENGTH = 4000;
    private static final int MAX_BANNER_URL_LENGTH = 2048;
    private static final int MAX_SLUG_LENGTH = 255;

    private static final Set<EventStatus> TERMINAL_STATUSES =
            EnumSet.of(EventStatus.CANCELLED, EventStatus.ARCHIVED);

    private final Long id;
    private final Long organizationId;
    private Long venueId;
    private String title;
    private String slug;
    private String description;
    private String bannerUrl;
    private EventStatus status;
    private Instant publishedAt;

    public static Event create(
            Long organizationId,
            String title,
            String slug,
            String description,
            String bannerUrl,
            Long venueId
    ) {
        if (organizationId == null) {
            throw new DomainValidationException("Event organization id cannot be null.");
        }

        validateTitle(title);
        validateSlug(slug);
        validateDescription(description);
        validateBannerUrl(bannerUrl);

        return Event.builder()
                .organizationId(organizationId)
                .title(title.trim())
                .slug(slug.trim().toLowerCase())
                .description(description)
                .bannerUrl(bannerUrl)
                .venueId(venueId)
                .status(EventStatus.DRAFT)
                .build();
    }

    public void rename(String title, String slug) {
        ensureNotTerminal();
        validateTitle(title);
        validateSlug(slug);
        this.title = title.trim();
        this.slug = slug.trim().toLowerCase();
    }

    public void changeDescription(String description) {
        ensureNotTerminal();
        validateDescription(description);
        this.description = description;
    }

    public void changeBanner(String bannerUrl) {
        ensureNotTerminal();
        validateBannerUrl(bannerUrl);
        this.bannerUrl = bannerUrl;
    }

    public void assignVenue(Long venueId) {
        ensureNotTerminal();

        if (venueId == null) {
            throw new DomainValidationException("Venue id cannot be null.");
        }

        this.venueId = venueId;
    }

    public void publish() {
        if (status == EventStatus.PUBLISHED) {
            throw new DomainConflictException("Event is already published.");
        }

        if (status == EventStatus.CANCELLED) {
            throw new DomainConflictException("Cancelled event cannot be published. Reopen first.");
        }

        if (status == EventStatus.ARCHIVED) {
            throw new DomainConflictException("Archived event cannot be published. Restore first.");
        }

        if (status == EventStatus.ENDED) {
            throw new DomainConflictException("Ended event cannot be published.");
        }

        if (status == EventStatus.LIVE) {
            throw new DomainConflictException("Live event cannot be published.");
        }

        if (venueId == null) {
            throw new DomainConflictException("Event must have a venue before it can be published.");
        }

        status = EventStatus.PUBLISHED;
        publishedAt = publishedAt == null ? Instant.now() : publishedAt;
    }

    public void pauseSales() {
        if (status == EventStatus.SALES_PAUSED) {
            throw new DomainConflictException("Event sales are already paused.");
        }

        if (status != EventStatus.PUBLISHED && status != EventStatus.LIVE) {
            throw new DomainConflictException("Only published or live events can pause sales.");
        }

        status = EventStatus.SALES_PAUSED;
    }

    public void resumeSales() {
        if (status != EventStatus.SALES_PAUSED) {
            throw new DomainConflictException("Only events with paused sales can resume.");
        }

        status = EventStatus.PUBLISHED;
    }

    public void cancel(String reason) {
        ensureNotTerminal();

        if (reason == null || reason.isBlank()) {
            throw new DomainValidationException("Cancellation reason is mandatory.");
        }

        status = EventStatus.CANCELLED;
    }

    public void reopen() {
        if (status != EventStatus.CANCELLED) {
            throw new DomainConflictException("Only cancelled events can be reopened.");
        }

        status = EventStatus.DRAFT;
    }

    public void archive() {
        if (status == EventStatus.ARCHIVED) {
            throw new DomainConflictException("Event is already archived.");
        }

        if (status == EventStatus.CANCELLED || status == EventStatus.LIVE) {
            throw new DomainConflictException("Event cannot be archived in its current state.");
        }

        status = EventStatus.ARCHIVED;
    }

    public void restore() {
        if (status != EventStatus.ARCHIVED) {
            throw new DomainConflictException("Only archived events can be restored.");
        }

        status = EventStatus.DRAFT;
    }

    public void markStarted() {
        if (status != EventStatus.PUBLISHED && status != EventStatus.SALES_PAUSED) {
            throw new DomainConflictException("Only published events can go live.");
        }

        status = EventStatus.LIVE;
    }

    public void markEnded() {
        if (status != EventStatus.LIVE) {
            throw new DomainConflictException("Only live events can be ended.");
        }

        status = EventStatus.ENDED;
    }

    public boolean isActive() {
        return status == EventStatus.PUBLISHED || status == EventStatus.LIVE;
    }

    public boolean isTerminal() {
        return TERMINAL_STATUSES.contains(status);
    }

    private void ensureNotTerminal() {
        if (isTerminal()) {
            throw new DomainConflictException("Event is in a terminal state and cannot be modified.");
        }
    }

    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new DomainValidationException("Event title cannot be blank.");
        }

        if (title.length() > MAX_TITLE_LENGTH) {
            throw new DomainValidationException("Event title must not exceed " + MAX_TITLE_LENGTH + " characters.");
        }
    }

    private static void validateSlug(String slug) {
        if (slug == null || slug.isBlank()) {
            throw new DomainValidationException("Event slug cannot be blank.");
        }

        if (slug.length() > MAX_SLUG_LENGTH) {
            throw new DomainValidationException("Event slug must not exceed " + MAX_SLUG_LENGTH + " characters.");
        }

        if (!slug.matches("^[a-z0-9-]+$")) {
            throw new DomainValidationException("Event slug must be lowercase alphanumeric with hyphens only.");
        }
    }

    private static void validateDescription(String description) {
        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new DomainValidationException("Event description must not exceed " + MAX_DESCRIPTION_LENGTH + " characters.");
        }
    }

    private static void validateBannerUrl(String bannerUrl) {
        if (bannerUrl == null || bannerUrl.isBlank()) {
            return;
        }

        if (bannerUrl.length() > MAX_BANNER_URL_LENGTH) {
            throw new DomainValidationException("Event bannerUrl must not exceed " + MAX_BANNER_URL_LENGTH + " characters.");
        }

        try {
            URI uri = new URI(bannerUrl);
            if (!uri.isAbsolute() || !("http".equals(uri.getScheme()) || "https".equals(uri.getScheme()))) {
                throw new DomainValidationException("Event bannerUrl must be a valid HTTP(S) URL.");
            }
        } catch (URISyntaxException e) {
            throw new DomainValidationException("Event bannerUrl must be a valid HTTP(S) URL.");
        }
    }
}
