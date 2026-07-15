package com.ute.ticket.search.application.service;

import com.ute.ticket.search.application.command.SearchEventCommand;
import com.ute.ticket.search.application.port.in.SearchEventUseCase;
import com.ute.ticket.search.application.port.out.EventSearchRepository;
import com.ute.ticket.search.application.result.EventSearchResult;
import com.ute.ticket.search.infrastructure.elasticsearch.document.EventDocument;
import com.ute.ticket.shared.dto.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchEventService implements SearchEventUseCase {

    private final EventSearchRepository eventSearchRepository;

    @Override
    public PageInfo<EventSearchResult> execute(SearchEventCommand cmd) {
        try {
            PageInfo<EventDocument> page = eventSearchRepository.search(cmd);

            return PageInfo.<EventSearchResult>builder()
                    .pageContent(page.getPageContent().stream().map(this::toResult).toList())
                    .number(page.getNumber())
                    .size(page.getSize())
                    .totalElements(page.getTotalElements())
                    .totalPages(page.getTotalPages())
                    .empty(page.isEmpty())
                    .numberOfElements(page.getNumberOfElements())
                    .hasNextPage(page.isHasNextPage())
                    .hasPreviousPage(page.isHasPreviousPage())
                    .build();

        }
        catch (Exception e) {
            System.out.println(e.getMessage());;
        }
        return null;
    }

    private EventSearchResult toResult(EventDocument doc) {
        return new EventSearchResult(
                doc.getId(),
                doc.getTitle(),
                doc.getDescription(),
                doc.getStatus(),
                doc.getBannerUrl(),
                doc.getOrganizationId(),
                doc.getOrganizationName(),
                doc.getVenueId(),
                doc.getVenueName(),
                doc.getVenueCity(),
                doc.getCategoryIds(),
                doc.getCategoryNames(),
                doc.getCategorySlugs(),
                doc.getMinPrice(),
                doc.getMaxPrice(),
                doc.getHasAvailableTickets(),
                doc.getPublishedAt(),
                doc.getCreatedAt(),
                doc.getUpdatedAt()
        );
    }
}
