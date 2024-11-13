package org.example.controllers;

import org.example.dto.CategoryDto;
import org.example.services.interfaces.CategoryService;
import org.example.annotations.CategoryAnnotations.GetAllCategories;
import org.example.annotations.CategoryAnnotations.GetCategoryById;
import org.example.annotations.CategoryAnnotations.CreateCategory;
import org.example.annotations.CategoryAnnotations.UpdateCategory;
import org.example.annotations.CategoryAnnotations.DeleteCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    // Конструкторная инъекция
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Получить все категории
    @GetAllCategories
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Получить категорию по ID
    @GetCategoryById
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        Optional<CategoryDto> categoryOpt = categoryService.getCategoryById(id);
        return categoryOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Создать новую категорию
    @CreateCategory
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto created = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(201).body(created);
    }

    // Обновить категорию
    @UpdateCategory
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        Optional<CategoryDto> updatedOpt = categoryService.updateCategory(id, categoryDto);
        return updatedOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Удалить категорию
    @DeleteCategory
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
