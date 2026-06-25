package com.ute.ticket.event.application.service;

import com.ute.ticket.event.application.command.DeleteCategoryCommand;
import com.ute.ticket.event.application.port.in.DeleteCategoryUseCase;
import com.ute.ticket.event.application.port.out.CategoryRepository;
import com.ute.ticket.event.application.port.out.EventCategoryRepository;
import com.ute.ticket.event.application.result.CategoryResult;
import com.ute.ticket.event.domain.entity.Category;
import com.ute.ticket.shared.exception.ConflictException;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteCategoryService implements DeleteCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final EventCategoryRepository eventCategoryRepository;

    @Override
    public CategoryResult execute(DeleteCategoryCommand cmd) {
        Category category = categoryRepository.findById(cmd.getId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (eventCategoryRepository.existsByCategoryId(cmd.getId())) {
            throw new ConflictException("Category is assigned to events and cannot be deleted");
        }

        category.markDeleted();
        category = categoryRepository.save(category);

        return CategoryResult.from(category);
    }
}
