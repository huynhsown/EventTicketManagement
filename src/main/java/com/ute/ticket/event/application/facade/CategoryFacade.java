package com.ute.ticket.event.application.facade;

import com.ute.ticket.event.application.command.ArchiveCategoryCommand;
import com.ute.ticket.event.application.command.CreateCategoryCommand;
import com.ute.ticket.event.application.command.DeleteCategoryCommand;
import com.ute.ticket.event.application.command.RenameCategoryCommand;
import com.ute.ticket.event.application.command.RestoreCategoryCommand;
import com.ute.ticket.event.application.port.in.ArchiveCategoryUseCase;
import com.ute.ticket.event.application.port.in.CreateCategoryUseCase;
import com.ute.ticket.event.application.port.in.DeleteCategoryUseCase;
import com.ute.ticket.event.application.port.in.RenameCategoryUseCase;
import com.ute.ticket.event.application.port.in.RestoreCategoryUseCase;
import com.ute.ticket.event.application.result.CategoryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryFacade {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final RenameCategoryUseCase renameCategoryUseCase;
    private final ArchiveCategoryUseCase archiveCategoryUseCase;
    private final RestoreCategoryUseCase restoreCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryResult createCategory(CreateCategoryCommand cmd) {
        return createCategoryUseCase.execute(cmd);
    }

    public CategoryResult renameCategory(RenameCategoryCommand cmd) {
        return renameCategoryUseCase.execute(cmd);
    }

    public CategoryResult archiveCategory(Long id) {
        var command = ArchiveCategoryCommand.builder()
                .id(id)
                .build();
        return archiveCategoryUseCase.execute(command);
    }

    public CategoryResult restoreCategory(Long id) {
        var command = RestoreCategoryCommand.builder()
                .id(id)
                .build();
        return restoreCategoryUseCase.execute(command);
    }

    public CategoryResult deleteCategory(Long id) {
        var command = DeleteCategoryCommand.builder()
                .id(id)
                .build();
        return deleteCategoryUseCase.execute(command);
    }
}
