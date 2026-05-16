package com.ute.ticket.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageInfo<T> {
    private List<T> pageContent;
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean empty;
    private int numberOfElements;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
}
