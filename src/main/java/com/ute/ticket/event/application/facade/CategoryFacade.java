package com.ute.ticket.event.application.facade;

import com.ute.ticket.event.application.command.CreateCategoryCommand;
import com.ute.ticket.event.application.port.in.CreateCategoryUseCase;
import com.ute.ticket.event.application.result.CategoryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryFacade {

    private final CreateCategoryUseCase createCategoryUseCase;

    public CategoryResult createCategory(CreateCategoryCommand cmd) {
        return createCategoryUseCase.execute(cmd);
    }
}
