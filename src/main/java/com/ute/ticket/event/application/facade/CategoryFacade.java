package com.ute.ticket.event.application.facade;

import com.ute.ticket.event.application.command.CreateCategoryCommand;
import com.ute.ticket.event.application.command.RenameCategoryCommand;
import com.ute.ticket.event.application.port.in.CreateCategoryUseCase;
import com.ute.ticket.event.application.port.in.RenameCategoryUseCase;
import com.ute.ticket.event.application.result.CategoryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryFacade {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final RenameCategoryUseCase renameCategoryUseCase;

    public CategoryResult createCategory(CreateCategoryCommand cmd) {
        return createCategoryUseCase.execute(cmd);
    }

    public CategoryResult renameCategory(RenameCategoryCommand cmd) {
        return renameCategoryUseCase.execute(cmd);
    }
}
