package com.ute.ticket.event.presentation;

import com.ute.ticket.event.application.facade.EventFacade;
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
import com.ute.ticket.event.presentation.dto.EventDetailResponse.Category;
import com.ute.ticket.event.presentation.dto.EventDetailResponse.Inventory;
import com.ute.ticket.event.presentation.dto.EventDetailResponse.Organization;
import com.ute.ticket.event.presentation.dto.EventDetailResponse.Session;
import com.ute.ticket.event.presentation.dto.EventDetailResponse.TicketType;
import com.ute.ticket.event.presentation.dto.EventDetailResponse.Venue;
import com.ute.ticket.event.presentation.mapper.AssignVenueMapper;
import com.ute.ticket.event.presentation.mapper.CreateEventMapper;
import com.ute.ticket.event.presentation.mapper.EventDetailMapper;
import com.ute.ticket.search.application.port.in.SearchEventUseCase;
import com.ute.ticket.search.application.result.EventSearchResult;
import com.ute.ticket.search.presentation.SearchEventController;
import com.ute.ticket.search.presentation.mapper.SearchEventMapper;
import com.ute.ticket.shared.application.security.CurrentUser;
import com.ute.ticket.shared.dto.PageInfo;
import com.ute.ticket.shared.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {EventController.class, SearchEventController.class})
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private EventFacade eventFacade;
    @MockitoBean private CreateEventMapper createEventMapper;
    @MockitoBean private AssignVenueMapper assignVenueMapper;
    @MockitoBean private EventDetailMapper eventDetailMapper;
    @MockitoBean private CurrentUser currentUser;
    @MockitoBean private SearchEventUseCase searchEventUseCase;
    @MockitoBean private SearchEventMapper searchEventMapper;

    @Test
    void getEventDetailReturnsSuccessEnvelope() throws Exception {
        when(eventFacade.getEventDetail("ute-conf-2027")).thenReturn(detail());
        when(eventDetailMapper.toResponse(any())).thenReturn(response());

        mockMvc.perform(get("/api/events/ute-conf-2027"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Event detail retrieved successfully"))
                .andExpect(jsonPath("$.data.title").value("UTE Software Engineering Conference 2027"))
                .andExpect(jsonPath("$.data.status").value("PUBLISHED"))
                .andExpect(jsonPath("$.data.organization.name").value("UTE Events"))
                .andExpect(jsonPath("$.data.sessions[0].ticketTypes[0].inventory.available").value(80));
    }

    @Test
    void getEventDetailReturnsNotFoundEnvelope() throws Exception {
        when(eventFacade.getEventDetail("missing")).thenThrow(new NotFoundException("Event not found"));

        mockMvc.perform(get("/api/events/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Event not found"));
    }

    @Test
    void searchLiteralRouteWinsOverSlugPathVariable() throws Exception {
        PageInfo<EventSearchResult> page = PageInfo.<EventSearchResult>builder()
                .pageContent(List.of())
                .number(0)
                .size(10)
                .totalElements(0L)
                .totalPages(0)
                .empty(true)
                .numberOfElements(0)
                .hasNextPage(false)
                .hasPreviousPage(false)
                .build();
        when(searchEventUseCase.execute(any())).thenReturn(page);

        mockMvc.perform(get("/api/events/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Events retrieved successfully"));

        verify(eventFacade, never()).getEventDetail(any());
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
                        Instant.parse("2026-09-01T08:00:00Z"),
                        Instant.parse("2026-09-01T09:00:00Z"),
                        Instant.parse("2026-08-01T08:00:00Z"),
                        Instant.parse("2026-08-31T08:00:00Z"),
                        SessionStatus.LIVE,
                        List.of(new TicketTypeDetail(
                                21L, "VIP", "best", new BigDecimal("150.00"), 2,
                                TicketTypeStatus.ACTIVE, new InventoryInfo(80, 20, 100))))),
                Instant.parse("2026-08-01T08:00:00Z"),
                Instant.parse("2026-08-01T08:00:00Z"),
                Instant.parse("2026-08-01T08:00:00Z")
        );
    }

    private EventDetailResponse response() {
        return new EventDetailResponse(
                1L,
                "UTE Software Engineering Conference 2027",
                "A conference",
                EventStatus.PUBLISHED,
                false,
                "http://example.com/banner.png",
                new Organization(1L, "UTE Events", "http://example.com/logo.png", "ute-events"),
                new Venue(3L, "Riverside Event Hall", "123 River St", "Ho Chi Minh City"),
                List.of(new Category(4L, "Education", "education")),
                List.of(new Session(
                        10L,
                        Instant.parse("2026-09-01T08:00:00Z"),
                        Instant.parse("2026-09-01T09:00:00Z"),
                        Instant.parse("2026-08-01T08:00:00Z"),
                        Instant.parse("2026-08-31T08:00:00Z"),
                        SessionStatus.LIVE,
                        List.of(new TicketType(
                                21L, "VIP", "best", new BigDecimal("150.00"), 2,
                                TicketTypeStatus.ACTIVE, new Inventory(80, 20, 100))))),
                Instant.parse("2026-08-01T08:00:00Z"),
                Instant.parse("2026-08-01T08:00:00Z"),
                Instant.parse("2026-08-01T08:00:00Z")
        );
    }
}
