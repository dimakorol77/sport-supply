package org.example.services.interfaces;

import org.example.models.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(Long id);
    Category createCategory(Category category);
    Optional<Category> updateCategory(Long id, Category updatedCategory);
    void deleteCategory(Long id);
}
