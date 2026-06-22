package com.ute.ticket.event.application.service;

import com.ute.ticket.event.application.command.UpdateCategoryCommand;
import com.ute.ticket.event.application.port.in.UpdateCategoryUseCase;
import com.ute.ticket.event.application.port.out.CategoryRepository;
import com.ute.ticket.event.application.result.CategoryResult;
import com.ute.ticket.event.domain.entity.Category;
import com.ute.ticket.event.domain.event.CategoryUpdated;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateCategoryService implements UpdateCategoryUseCase {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResult execute(UpdateCategoryCommand cmd) {
        Category category = categoryRepository.findById(cmd.getId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (cmd.getDescription() != null) {
            category.changeDescription(cmd.getDescription());
        }

        if (cmd.getDisplayOrder() != null) {
            category.changeDisplayOrder(cmd.getDisplayOrder());
        }

        category = categoryRepository.save(category);
        return CategoryResult.from(category);
    }
}
