package com.ute.ticket.search.infrastructure.elasticsearch.adapter;

import com.ute.ticket.search.application.port.out.EventIndexer;
import com.ute.ticket.search.infrastructure.elasticsearch.document.EventDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ElasticsearchEventIndexer implements EventIndexer {

    private static final IndexCoordinates INDEX = IndexCoordinates.of("events");

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public void index(EventDocument document) {
        elasticsearchOperations.save(document);
    }

    @Override
    public void updateStatus(Long eventId, String status, Instant publishedAt) {
        Document document = Document.create();
        document.put("status", status);
        if (publishedAt != null) {
            document.put("publishedAt", publishedAt);
        }

        UpdateQuery updateQuery = UpdateQuery.builder(String.valueOf(eventId))
                .withDocument(document)
                .build();

        elasticsearchOperations.update(updateQuery, INDEX);
    }

    @Override
    public void delete(Long eventId) {
        elasticsearchOperations.delete(String.valueOf(eventId), INDEX);
    }
}
