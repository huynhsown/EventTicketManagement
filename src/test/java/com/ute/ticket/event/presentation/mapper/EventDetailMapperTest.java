package com.ute.ticket.event.presentation.mapper;

import com.ute.ticket.event.application.result.EventDetailResult;
import com.ute.ticket.event.application.result.EventDetailResult.CategoryRef;
import com.ute.ticket.event.application.result.EventDetailResult.InventoryInfo;
import com.ute.ticket.event.application.result.EventDetailResult.OrganizationRef;
import com.ute.ticket.event.application.result.EventDetailResult.SessionDetail;
import com.ute.ticket.event.application.result.EventDetailResult.TicketTypeDetail;
import com.ute.ticket.event.application.result.EventDetailResult.VenueRef;
import com.ute.ticket.event.domain.enums.EventStatus;
import com.ute.ticket.event.domain.enums.SessionStatus;
import com.ute.ticket.event.domain.enums.TicketTypeStatus;
import com.ute.ticket.event.presentation.dto.EventDetailResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EventDetailMapperTest {

    private static final Instant NOW = Instant.parse("2026-08-04T10:00:00Z");

    private final EventDetailMapper mapper = new EventDetailMapper();

    @Test
    void mapsCompleteDetail() {
        EventDetailResponse response = mapper.toResponse(detail());

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("UTE Software Engineering Conference 2027");
        assertThat(response.status()).isEqualTo(EventStatus.PUBLISHED);
        assertThat(response.salesPaused()).isFalse();
        assertThat(response.organization()).isNotNull();
        assertThat(response.organization().name()).isEqualTo("UTE Events");
        assertThat(response.organization().slug()).isEqualTo("ute-events");
        assertThat(response.venue()).isNotNull();
        assertThat(response.venue().city()).isEqualTo("Ho Chi Minh City");
        assertThat(response.categories()).extracting("slug").containsExactly("education");
        assertThat(response.sessions()).hasSize(1);
        assertThat(response.sessions().get(0).ticketTypes()).hasSize(1);
        EventDetailResponse.TicketType ticketType = response.sessions().get(0).ticketTypes().get(0);
        assertThat(ticketType.name()).isEqualTo("VIP");
        assertThat(ticketType.price()).isEqualByComparingTo(new BigDecimal("150.00"));
        assertThat(ticketType.inventory().available()).isEqualTo(80);
        assertThat(ticketType.inventory().sold()).isEqualTo(20);
        assertThat(ticketType.inventory().total()).isEqualTo(100);
        assertThat(response.publishedAt()).isEqualTo(NOW);
        assertThat(response.createdAt()).isEqualTo(NOW);
        assertThat(response.updatedAt()).isEqualTo(NOW);
    }

    @Test
    void mapsNullOrganizationAndVenueToNull() {
        EventDetailResult result = new EventDetailResult(
                1L, "T", null, EventStatus.LIVE, true, null,
                null, null, List.of(), List.of(), NOW, NOW, NOW);

        EventDetailResponse response = mapper.toResponse(result);

        assertThat(response.organization()).isNull();
        assertThat(response.venue()).isNull();
        assertThat(response.categories()).isEmpty();
        assertThat(response.sessions()).isEmpty();
        assertThat(response.salesPaused()).isTrue();
        assertThat(response.status()).isEqualTo(EventStatus.LIVE);
    }

    @Test
    void mapsNullResultToNull() {
        assertThat(mapper.toResponse(null)).isNull();
    }

    private EventDetailResult detail() {
        return new EventDetailResult(
                1L,
                "UTE Software Engineering Conference 2027",
                "A conference",
                EventStatus.PUBLISHED,
                false,
                "http://example.com/banner.png",
                new OrganizationRef(1L, "UTE Events", "http://example.com/logo.png", "ute-events"),
                new VenueRef(3L, "Riverside Event Hall", "123 River St", "Ho Chi Minh City"),
                List.of(new CategoryRef(4L, "Education", "education")),
                List.of(new SessionDetail(
                        10L,
                        NOW,
                        NOW.plusSeconds(3600),
                        NOW.minusSeconds(3600),
                        NOW,
                        SessionStatus.LIVE,
                        List.of(new TicketTypeDetail(
                                21L, "VIP", "best", new BigDecimal("150.00"), 2,
                                TicketTypeStatus.ACTIVE, new InventoryInfo(80, 20, 100))))),
                NOW,
                NOW,
                NOW
        );
    }
}
