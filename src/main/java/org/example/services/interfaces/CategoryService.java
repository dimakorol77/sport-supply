package org.example.services.interfaces;

import org.example.dto.CategoryDto;
import org.example.models.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    Optional<CategoryDto> getCategoryById(Long id);
    CategoryDto createCategory(CategoryDto categoryDto);
    Optional<CategoryDto> updateCategory(Long id, CategoryDto categoryDto);
    void deleteCategory(Long id);
}

