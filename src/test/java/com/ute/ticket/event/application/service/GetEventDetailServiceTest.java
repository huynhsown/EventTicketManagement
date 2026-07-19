package com.ute.ticket.event.application.service;

import com.ute.ticket.event.application.port.out.CategoryRepository;
import com.ute.ticket.event.application.port.out.EventCategoryRepository;
import com.ute.ticket.event.application.port.out.EventRepository;
import com.ute.ticket.event.application.port.out.InventoryRepository;
import com.ute.ticket.event.application.port.out.SessionRepository;
import com.ute.ticket.event.application.port.out.TicketTypeRepository;
import com.ute.ticket.event.application.result.EventDetailResult;
import com.ute.ticket.event.domain.entity.Category;
import com.ute.ticket.event.domain.entity.Event;
import com.ute.ticket.event.domain.entity.EventCategory;
import com.ute.ticket.event.domain.entity.Inventory;
import com.ute.ticket.event.domain.entity.Session;
import com.ute.ticket.event.domain.entity.TicketType;
import com.ute.ticket.event.domain.enums.EventStatus;
import com.ute.ticket.event.domain.enums.InventoryStatus;
import com.ute.ticket.event.domain.enums.SessionStatus;
import com.ute.ticket.event.domain.enums.TicketTypeStatus;
import com.ute.ticket.organization.application.port.out.OrganizationRepository;
import com.ute.ticket.organization.domain.entity.Organization;
import com.ute.ticket.shared.exception.NotFoundException;
import com.ute.ticket.venue.application.port.out.VenueRepository;
import com.ute.ticket.venue.domain.entity.Venue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetEventDetailServiceTest {

    private static final Instant NOW = Instant.parse("2026-08-04T10:00:00Z");

    @Mock private EventRepository eventRepository;
    @Mock private SessionRepository sessionRepository;
    @Mock private TicketTypeRepository ticketTypeRepository;
    @Mock private InventoryRepository inventoryRepository;
    @Mock private EventCategoryRepository eventCategoryRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private OrganizationRepository organizationRepository;
    @Mock private VenueRepository venueRepository;

    private GetEventDetailService service;

    @BeforeEach
    void setUp() {
        service = new GetEventDetailService(
                eventRepository, sessionRepository, ticketTypeRepository, inventoryRepository,
                eventCategoryRepository, categoryRepository, organizationRepository, venueRepository);
    }

    @Test
    void returnsCompleteProfileForPublishedEvent() {
        Event event = event(EventStatus.PUBLISHED);
        when(eventRepository.findBySlug("ute-conf-2027")).thenReturn(Optional.of(event));
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization()));
        when(venueRepository.findById(3L)).thenReturn(Optional.of(venue()));
        when(eventCategoryRepository.findByEventId(1L)).thenReturn(List.of(
                EventCategory.builder().eventId(1L).categoryId(6L).build(),
                EventCategory.builder().eventId(1L).categoryId(4L).build()));
        when(categoryRepository.findByIdsIn(List.of(6L, 4L))).thenReturn(List.of(
                category(6L, "Gaming", "gaming"),
                category(4L, "Education", "education")));
        when(sessionRepository.findByEventId(1L)).thenReturn(List.of());

        EventDetailResult detail = service.execute("ute-conf-2027");

        assertThat(detail.id()).isEqualTo(1L);
        assertThat(detail.title()).isEqualTo("UTE Software Engineering Conference 2027");
        assertThat(detail.description()).isEqualTo("A conference");
        assertThat(detail.status()).isEqualTo(EventStatus.PUBLISHED);
        assertThat(detail.salesPaused()).isFalse();
        assertThat(detail.bannerUrl()).isEqualTo("http://example.com/banner.png");
        assertThat(detail.organization()).isNotNull();
        assertThat(detail.organization().name()).isEqualTo("UTE Events");
        assertThat(detail.organization().logoUrl()).isEqualTo("http://example.com/logo.png");
        assertThat(detail.organization().slug()).isEqualTo("ute-events");
        assertThat(detail.venue()).isNotNull();
        assertThat(detail.venue().name()).isEqualTo("Riverside Event Hall");
        assertThat(detail.venue().city()).isEqualTo("Ho Chi Minh City");
        assertThat(detail.categories()).extracting("slug").containsExactly("education", "gaming");
        assertThat(detail.sessions()).isEmpty();
        assertThat(detail.publishedAt()).isEqualTo(NOW);
        assertThat(detail.createdAt()).isEqualTo(NOW);
        assertThat(detail.updatedAt()).isEqualTo(NOW);
    }

    @Test
    void returnsEmptyVenueWhenEventHasNoVenueAssigned() {
        Event event = Event.builder()
                .id(1L).organizationId(1L).venueId(null)
                .title("T").slug("t").status(EventStatus.PUBLISHED).build();
        when(eventRepository.findBySlug("t")).thenReturn(Optional.of(event));
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization()));
        when(eventCategoryRepository.findByEventId(1L)).thenReturn(List.of());
        when(sessionRepository.findByEventId(1L)).thenReturn(List.of());

        EventDetailResult detail = service.execute("t");

        assertThat(detail.venue()).isNull();
    }

    @Test
    void includesOnlyOnSaleSessionsWithActiveTicketTypesAndCorrectInventory() {
        Event event = event(EventStatus.PUBLISHED);
        Session live = session(10L, SessionStatus.LIVE, Instant.parse("2026-09-01T08:00:00Z"));
        Session cancelled = session(11L, SessionStatus.CANCELLED, Instant.parse("2026-09-02T08:00:00Z"));

        when(eventRepository.findBySlug("ute-conf-2027")).thenReturn(Optional.of(event));
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization()));
        when(eventCategoryRepository.findByEventId(1L)).thenReturn(List.of());
        when(sessionRepository.findByEventId(1L)).thenReturn(List.of(live, cancelled));

        TicketType vip = ticketType(21L, "VIP", new BigDecimal("150.00"));
        TicketType standard = ticketType(22L, "Standard", new BigDecimal("50.00"));
        TicketType disabled = TicketType.builder()
                .id(23L).sessionId(10L).name("Discontinued").price(new BigDecimal("5.00")).maxPerUser(2)
                .status(TicketTypeStatus.DISABLED).build();

        when(ticketTypeRepository.findBySessionIdsIn(List.of(10L)))
                .thenReturn(List.of(vip, disabled, standard));
        when(inventoryRepository.findByIdsIn(List.of(22L, 21L))).thenReturn(List.of(
                inventory(22L, 500, 0, 50),
                inventory(21L, 100, 0, 20)));

        EventDetailResult detail = service.execute("ute-conf-2027");

        assertThat(detail.sessions()).hasSize(1);
        EventDetailResult.SessionDetail sessionDetail = detail.sessions().get(0);
        assertThat(sessionDetail.id()).isEqualTo(10L);
        assertThat(sessionDetail.status()).isEqualTo(SessionStatus.LIVE);

        assertThat(sessionDetail.ticketTypes())
                .extracting("name")
                .containsExactly("Standard", "VIP");

        EventDetailResult.TicketTypeDetail standardDetail = sessionDetail.ticketTypes().get(0);
        assertThat(standardDetail.inventory().available()).isEqualTo(450);
        assertThat(standardDetail.inventory().sold()).isEqualTo(50);
        assertThat(standardDetail.inventory().total()).isEqualTo(500);

        EventDetailResult.TicketTypeDetail vipDetail = sessionDetail.ticketTypes().get(1);
        assertThat(vipDetail.inventory().available()).isEqualTo(80);
        assertThat(vipDetail.inventory().sold()).isEqualTo(20);
        assertThat(vipDetail.inventory().total()).isEqualTo(100);
    }

    @Test
    void showsSoldOutActiveTicketTypeWithZeroAvailable() {
        Event event = event(EventStatus.PUBLISHED);
        when(eventRepository.findBySlug("ute-conf-2027")).thenReturn(Optional.of(event));
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization()));
        when(eventCategoryRepository.findByEventId(1L)).thenReturn(List.of());
        when(sessionRepository.findByEventId(1L)).thenReturn(List.of(session(10L, SessionStatus.LIVE, Instant.parse("2026-09-01T08:00:00Z"))));
        when(ticketTypeRepository.findBySessionIdsIn(List.of(10L)))
                .thenReturn(List.of(ticketType(21L, "VIP", new BigDecimal("150.00"))));
        when(inventoryRepository.findByIdsIn(List.of(21L))).thenReturn(List.of(inventory(21L, 10, 0, 10)));

        EventDetailResult detail = service.execute("ute-conf-2027");

        EventDetailResult.TicketTypeDetail vipDetail = detail.sessions().get(0).ticketTypes().get(0);
        assertThat(vipDetail.inventory().available()).isZero();
        assertThat(vipDetail.inventory().total()).isEqualTo(10);
    }

    @Test
    void treatsMissingInventoryAsSoldOut() {
        Event event = event(EventStatus.PUBLISHED);
        when(eventRepository.findBySlug("ute-conf-2027")).thenReturn(Optional.of(event));
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization()));
        when(eventCategoryRepository.findByEventId(1L)).thenReturn(List.of());
        when(sessionRepository.findByEventId(1L)).thenReturn(List.of(session(10L, SessionStatus.LIVE, Instant.parse("2026-09-01T08:00:00Z"))));
        when(ticketTypeRepository.findBySessionIdsIn(List.of(10L)))
                .thenReturn(List.of(ticketType(21L, "VIP", new BigDecimal("150.00"))));
        when(inventoryRepository.findByIdsIn(List.of(21L))).thenReturn(List.of());

        EventDetailResult detail = service.execute("ute-conf-2027");

        EventDetailResult.TicketTypeDetail vipDetail = detail.sessions().get(0).ticketTypes().get(0);
        assertThat(vipDetail.inventory().available()).isZero();
        assertThat(vipDetail.inventory().sold()).isZero();
        assertThat(vipDetail.inventory().total()).isZero();
    }

    @Test
    void returnsSalesPausedEventWithIndicator() {
        Event event = event(EventStatus.SALES_PAUSED);
        when(eventRepository.findBySlug("ute-conf-2027")).thenReturn(Optional.of(event));
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization()));
        when(eventCategoryRepository.findByEventId(1L)).thenReturn(List.of());
        when(sessionRepository.findByEventId(1L)).thenReturn(List.of());

        EventDetailResult detail = service.execute("ute-conf-2027");

        assertThat(detail.status()).isEqualTo(EventStatus.SALES_PAUSED);
        assertThat(detail.salesPaused()).isTrue();
    }

    @Test
    void throwsNotFoundForUnknownSlug() {
        when(eventRepository.findBySlug("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.execute("missing"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Event not found");
    }

    @Test
    void throwsNotFoundWithIdenticalMessageForNonPublicEvents() {
        for (EventStatus status : List.of(EventStatus.DRAFT, EventStatus.CANCELLED, EventStatus.ARCHIVED, EventStatus.ENDED)) {
            Event event = event(status);
            when(eventRepository.findBySlug("ute-conf-2027")).thenReturn(Optional.of(event));

            assertThatThrownBy(() -> service.execute("ute-conf-2027"))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Event not found");
        }
    }

    private Event event(EventStatus status) {
        return Event.builder()
                .id(1L)
                .organizationId(1L)
                .venueId(3L)
                .title("UTE Software Engineering Conference 2027")
                .slug("ute-conf-2027")
                .description("A conference")
                .bannerUrl("http://example.com/banner.png")
                .status(status)
                .publishedAt(NOW)
                .createdAt(NOW)
                .updatedAt(NOW)
                .version(1L)
                .build();
    }

    private Organization organization() {
        return Organization.builder()
                .id(1L)
                .name("UTE Events")
                .logoUrl("http://example.com/logo.png")
                .slug("ute-events")
                .build();
    }

    private Venue venue() {
        return Venue.builder()
                .id(3L)
                .name("Riverside Event Hall")
                .address("123 River St")
                .city("Ho Chi Minh City")
                .build();
    }

    private Category category(Long id, String name, String slug) {
        return Category.builder().id(id).name(name).slug(slug).build();
    }

    private Session session(Long id, SessionStatus status, Instant startTime) {
        return Session.builder()
                .id(id)
                .eventId(1L)
                .startTime(startTime)
                .endTime(startTime.plusSeconds(3600))
                .salesStartAt(startTime.minusSeconds(3600))
                .salesEndAt(startTime)
                .status(status)
                .build();
    }

    private TicketType ticketType(Long id, String name, BigDecimal price) {
        return TicketType.builder()
                .id(id)
                .sessionId(10L)
                .name(name)
                .price(price)
                .maxPerUser(2)
                .status(TicketTypeStatus.ACTIVE)
                .build();
    }

    private Inventory inventory(Long ticketTypeId, int total, int reserved, int sold) {
        return Inventory.builder()
                .ticketTypeId(ticketTypeId)
                .totalStock(total)
                .reservedStock(reserved)
                .soldStock(sold)
                .status(InventoryStatus.ACTIVE)
                .build();
    }
}
