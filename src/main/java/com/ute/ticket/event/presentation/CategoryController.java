package com.ute.ticket.event.presentation;

import com.ute.ticket.event.application.facade.CategoryFacade;
import com.ute.ticket.event.application.result.CategoryResult;
import com.ute.ticket.event.presentation.dto.CreateCategoryRequest;
import com.ute.ticket.event.presentation.dto.RenameCategoryRequest;
import com.ute.ticket.event.presentation.mapper.CreateCategoryMapper;
import com.ute.ticket.event.presentation.mapper.RenameCategoryMapper;
import com.ute.ticket.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "Category endpoints")
public class CategoryController {

    private final CategoryFacade categoryFacade;
    private final CreateCategoryMapper createCategoryMapper;
    private final RenameCategoryMapper renameCategoryMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new category (system admin)")
    public ApiResponse<CategoryResult> create(@Valid @RequestBody CreateCategoryRequest request) {
        var command = createCategoryMapper.toCommand(request);
        var result = categoryFacade.createCategory(command);
        return ApiResponse.<CategoryResult>builder()
                .success(true)
                .message("Category created successfully")
                .data(result)
                .build();
    }

    @PatchMapping("/{id}/rename")
    @Operation(summary = "Rename a category and its slug (system admin / organization admin)")
    public ApiResponse<CategoryResult> rename(
            @PathVariable Long id,
            @Valid @RequestBody RenameCategoryRequest request
    ) {
        var command = renameCategoryMapper.toCommand(id, request);
        var result = categoryFacade.renameCategory(command);
        return ApiResponse.<CategoryResult>builder()
                .success(true)
                .message("Category renamed successfully")
                .data(result)
                .build();
    }
}
