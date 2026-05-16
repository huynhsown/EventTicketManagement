package com.ute.ticket.shared.dto;

import com.ute.ticket.shared.exception.BadRequestException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class PageRequestDTO {
    @Max(value = Integer.MAX_VALUE / 100, message = "Page to big")
    @Min(value = 0, message = "Page can not be less than zero")
    private int page = 0;
    @Max(value = 100, message = "Size can not be more than 100")
    private int size = 10;
    private String sortBy = "id";
    private boolean ascending = true;

    public Pageable toPageable() {
        long offset = (long) page * size;
        if (offset > Integer.MAX_VALUE) {
            throw new BadRequestException(
                    String.format("Page offset too big: page=%d, size=%d", page, size)
            );
        }

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }
}
