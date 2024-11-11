package org.example.controllers;

import org.example.dto.CategoryDto;
import org.example.services.interfaces.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @GetMapping
    @Operation(summary = "Получение всех категорий",
            description = "Возвращает список всех категорий",
            tags = "Категории",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Категории найдены")
            }
    )
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Получить категорию по ID
    @GetMapping("/{id}")
    @Operation(summary = "Получение категории по ID",
            description = "Возвращает категорию с указанным ID",
            tags = "Категории",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Категория найдена"),
                    @ApiResponse(responseCode = "404", description = "Категория не найдена")
            }
    )
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        Optional<CategoryDto> categoryOpt = categoryService.getCategoryById(id);
        return categoryOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Создать новую категорию
    @PostMapping
    @Operation(summary = "Создание новой категории",
            description = "Создает новую категорию",
            tags = "Категории",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Категория успешно создана"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto created = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(201).body(created);
    }

    // Обновить категорию
    @PutMapping("/{id}")
    @Operation(summary = "Обновление категории",
            description = "Обновляет данные категории по указанному ID",
            tags = "Категории",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Категория успешно обновлена"),
                    @ApiResponse(responseCode = "404", description = "Категория не найдена"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        Optional<CategoryDto> updatedOpt = categoryService.updateCategory(id, categoryDto);
        return updatedOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Удалить категорию
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление категории",
            description = "Удаляет категорию по указанному ID",
            tags = "Категории",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Категория успешно удалена"),
                    @ApiResponse(responseCode = "404", description = "Категория не найдена")
            }
    )
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
