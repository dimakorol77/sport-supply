package org.example.services.impl;

import org.example.models.Category;
import org.example.repositories.CategoryRepository;
import org.example.services.interfaces.CategoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    // Используем конструкторную инъекцию
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Получить все категории
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Получить категорию по ID
    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // Создать новую категорию
    @Override
    public Category createCategory(Category category) {
        category.setCreatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    // Обновить категорию
    @Override
    public Optional<Category> updateCategory(Long id, Category updatedCategory) {
        return categoryRepository.findById(id).map(category -> {
            category.setName(updatedCategory.getName());
            category.setDescription(updatedCategory.getDescription());
            category.setParentCategory(updatedCategory.getParentCategory());
            category.setUpdatedAt(LocalDateTime.now());
            // Обновление других полей при необходимости
            return categoryRepository.save(category);
        });
    }

    // Удалить категорию
    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
