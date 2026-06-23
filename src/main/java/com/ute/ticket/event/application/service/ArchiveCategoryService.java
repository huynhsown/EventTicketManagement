package com.ute.ticket.event.application.service;

import com.ute.ticket.event.application.command.ArchiveCategoryCommand;
import com.ute.ticket.event.application.port.in.ArchiveCategoryUseCase;
import com.ute.ticket.event.application.port.out.CategoryRepository;
import com.ute.ticket.event.application.result.CategoryResult;
import com.ute.ticket.event.domain.entity.Category;
import com.ute.ticket.event.domain.event.CategoryArchived;
import com.ute.ticket.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArchiveCategoryService implements ArchiveCategoryUseCase {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResult execute(ArchiveCategoryCommand cmd) {
        Category category = categoryRepository.findById(cmd.getId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        category.archive();
        category = categoryRepository.save(category);

        return CategoryResult.from(category);
    }
}
