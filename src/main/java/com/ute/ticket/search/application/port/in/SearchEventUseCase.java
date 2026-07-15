package com.ute.ticket.search.application.port.in;

import com.ute.ticket.search.application.command.SearchEventCommand;
import com.ute.ticket.search.application.result.EventSearchResult;
import com.ute.ticket.shared.dto.PageInfo;

public interface SearchEventUseCase {
    PageInfo<EventSearchResult> execute(SearchEventCommand cmd);
}
