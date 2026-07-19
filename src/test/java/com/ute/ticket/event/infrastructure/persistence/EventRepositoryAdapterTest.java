package com.ute.ticket.event.infrastructure.persistence;

import com.ute.ticket.event.domain.entity.Event;
import com.ute.ticket.event.domain.enums.EventStatus;
import com.ute.ticket.event.infrastructure.persistence.adapter.EventRepositoryAdapter;
import com.ute.ticket.event.infrastructure.persistence.jpa.entity.EventJpaEntity;
import com.ute.ticket.event.infrastructure.persistence.jpa.mapper.EventMapper;
import com.ute.ticket.event.infrastructure.persistence.jpa.repository.EventJpaRepository;
import com.ute.ticket.shared.config.JpaAuditConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@EnableJpaRepositories(basePackages = "com.ute.ticket.event.infrastructure.persistence.jpa.repository")
@EntityScan(basePackages = "com.ute.ticket.event.infrastructure.persistence.jpa.entity")
@Import({EventRepositoryAdapter.class, EventMapper.class, JpaAuditConfig.class})
class EventRepositoryAdapterTest {

    @Autowired
    private EventRepositoryAdapter eventRepositoryAdapter;

    @Autowired
    private EventJpaRepository eventJpaRepository;

    @Test
    void findBySlugMapsEventToDomain() {
        eventRepositoryAdapter.save(event(EventStatus.PUBLISHED, "ute-conf-2027"));

        Optional<Event> found = eventRepositoryAdapter.findBySlug("ute-conf-2027");

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("UTE Software Engineering Conference 2027");
        assertThat(found.get().getStatus()).isEqualTo(EventStatus.PUBLISHED);
        assertThat(found.get().getOrganizationId()).isEqualTo(1L);
        assertThat(found.get().getCreatedAt()).isNotNull();
        assertThat(found.get().getVersion()).isNotNull();
    }

    @Test
    void findBySlugReturnsEmptyForUnknownSlug() {
        assertThat(eventRepositoryAdapter.findBySlug("does-not-exist")).isEmpty();
    }

    @Test
    void findBySlugExcludesSoftDeletedEvent() {
        eventRepositoryAdapter.save(event(EventStatus.PUBLISHED, "ute-conf-2027"));

        EventJpaEntity entity = eventJpaRepository.findBySlugAndDeletedAtIsNull("ute-conf-2027").orElseThrow();
        entity.markDeleted();
        eventJpaRepository.save(entity);

        assertThat(eventRepositoryAdapter.findBySlug("ute-conf-2027")).isEmpty();
    }

    @Test
    void slugLookupIsExactMatchAfterNormalization() {
        eventRepositoryAdapter.save(event(EventStatus.PUBLISHED, "ute-conf-2027"));

        assertThat(eventJpaRepository.findBySlugAndDeletedAtIsNull("UTE-CONF-2027")).isEmpty();
    }

    private Event event(EventStatus status, String slug) {
        return Event.builder()
                .organizationId(1L)
                .venueId(3L)
                .title("UTE Software Engineering Conference 2027")
                .slug(slug)
                .description("A conference")
                .bannerUrl("http://example.com/banner.png")
                .status(status)
                .publishedAt(Instant.parse("2026-08-04T10:00:00Z"))
                .build();
    }
}
