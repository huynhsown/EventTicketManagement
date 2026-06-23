package com.ute.ticket.event.application.service;

import com.ute.ticket.event.application.command.RestoreCategoryCommand;
import com.ute.ticket.event.application.port.in.RestoreCategoryUseCase;
import com.ute.ticket.event.application.port.out.CategoryRepository;
import com.ute.ticket.event.application.result.CategoryResult;
import com.ute.ticket.event.domain.entity.Category;
import com.ute.ticket.event.domain.event.CategoryRestored;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestoreCategoryService implements RestoreCategoryUseCase {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResult execute(RestoreCategoryCommand cmd) {
        Category category = categoryRepository.findById(cmd.getId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        category.restore();
        category = categoryRepository.save(category);
        return CategoryResult.from(category);
    }
}
