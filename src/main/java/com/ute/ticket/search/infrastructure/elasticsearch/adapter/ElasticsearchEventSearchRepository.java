package com.ute.ticket.search.infrastructure.elasticsearch.adapter;

import com.ute.ticket.search.application.command.SearchEventCommand;
import com.ute.ticket.search.application.port.out.EventSearchRepository;
import com.ute.ticket.search.infrastructure.elasticsearch.document.EventDocument;
import com.ute.ticket.shared.dto.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ElasticsearchEventSearchRepository implements EventSearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public PageInfo<EventDocument> search(SearchEventCommand cmd) {
        List<Criteria> criteriaList = new ArrayList<>();

        if (cmd.getKeyword() != null && !cmd.getKeyword().isBlank()) {
            criteriaList.add(Criteria.where("title").matches(cmd.getKeyword())
                    .or(Criteria.where("description").matches(cmd.getKeyword())));
        }
        if (cmd.getCity() != null && !cmd.getCity().isBlank()) {
            criteriaList.add(Criteria.where("venueCity").matches(cmd.getCity()));
        }
        if (cmd.getCategorySlugs() != null && !cmd.getCategorySlugs().isEmpty()) {
            criteriaList.add(Criteria.where("categorySlugs").in(cmd.getCategorySlugs()));
        }
        if (cmd.getStatus() != null && !cmd.getStatus().isBlank()) {
            criteriaList.add(Criteria.where("status").is(cmd.getStatus()));
        }
        if (cmd.getMinPrice() != null) {
            criteriaList.add(Criteria.where("minPrice").greaterThanEqual(cmd.getMinPrice()));
        }
        if (cmd.getMaxPrice() != null) {
            criteriaList.add(Criteria.where("maxPrice").lessThanEqual(cmd.getMaxPrice()));
        }
        if (cmd.getHasAvailableTickets() != null) {
            criteriaList.add(Criteria.where("hasAvailableTickets").is(cmd.getHasAvailableTickets()));
        }

        Criteria criteria = criteriaList.isEmpty() ? new Criteria() : Criteria.and();
        criteria.and(criteriaList.toArray(new Criteria[0]));

        Sort sort = cmd.isAscending()
                ? Sort.by(resolveSortField(cmd.getSortBy())).ascending()
                : Sort.by(resolveSortField(cmd.getSortBy())).descending();

        PageRequest pageRequest = PageRequest.of(cmd.getPage(), cmd.getSize(), sort);
        CriteriaQuery query = new CriteriaQuery(criteria, pageRequest);

        SearchHits<EventDocument> searchHits = elasticsearchOperations.search(query, EventDocument.class);

        long totalElements = searchHits.getTotalHits();
        int totalPages = (int) Math.ceil((double) totalElements / cmd.getSize());
        boolean hasNext = cmd.getPage() < totalPages - 1;
        boolean hasPrevious = cmd.getPage() > 0 && totalPages > 0;

        return PageInfo.<EventDocument>builder()
                .pageContent(searchHits.getSearchHits().stream()
                        .map(hit -> hit.getContent())
                        .toList())
                .number(cmd.getPage())
                .size(cmd.getSize())
                .totalElements(totalElements)
                .totalPages(totalPages)
                .empty(searchHits.hasSearchHits() == false)
                .numberOfElements(searchHits.getSearchHits().size())
                .hasNextPage(hasNext)
                .hasPreviousPage(hasPrevious)
                .build();
    }

    private String resolveSortField(String sortBy) {
        return switch (sortBy) {
            case "title" -> "title.keyword";
            case "venueCity" -> "venueCity.keyword";
            case "organizationName" -> "organizationName.keyword";
            default -> sortBy;
        };
    }
}
