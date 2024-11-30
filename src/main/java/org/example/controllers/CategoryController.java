package org.example.controllers;

import org.example.dto.CategoryDto;
import org.example.services.interfaces.CategoryService;
import org.example.annotations.CategoryAnnotations.GetAllCategories;
import org.example.annotations.CategoryAnnotations.GetCategoryById;
import org.example.annotations.CategoryAnnotations.CreateCategory;
import org.example.annotations.CategoryAnnotations.UpdateCategory;
import org.example.annotations.CategoryAnnotations.DeleteCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetAllCategories
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }


    @GetCategoryById
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @CreateCategory
    public ResponseEntity<CategoryDto> createCategory(
            @Validated(CategoryDto.OnCreate.class) @RequestBody CategoryDto categoryDto) {
        CategoryDto created = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(201).body(created);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @UpdateCategory
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @Validated(CategoryDto.OnUpdate.class) @RequestBody CategoryDto categoryDto) {
        CategoryDto updated = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(updated);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteCategory
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
