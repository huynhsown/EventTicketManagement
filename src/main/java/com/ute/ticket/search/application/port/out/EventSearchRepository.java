package com.ute.ticket.search.application.port.out;

import com.ute.ticket.search.application.command.SearchEventCommand;
import com.ute.ticket.search.infrastructure.elasticsearch.document.EventDocument;
import com.ute.ticket.shared.dto.PageInfo;

public interface EventSearchRepository {

    PageInfo<EventDocument> search(SearchEventCommand cmd);
}
