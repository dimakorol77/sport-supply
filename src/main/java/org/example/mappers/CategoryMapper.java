package org.example.mappers;

import org.example.dto.CategoryDto;
import org.example.models.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setParentCategoryId(category.getParentCategory() != null ? category.getParentCategory().getId() : null);
        return dto;
    }

    public Category toEntity(CategoryDto dto) {
        if (dto == null) {
            return null;
        }
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        if (dto.getParentCategoryId() != null) {
            Category parentCategory = new Category();
            parentCategory.setId(dto.getParentCategoryId());
            category.setParentCategory(parentCategory);
        }
        return category;
    }

    public void updateEntityFromDto(CategoryDto dto, Category category) {
        if (dto == null || category == null) {
            return;
        }
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        if (dto.getParentCategoryId() != null) {
            Category parentCategory = new Category();
            parentCategory.setId(dto.getParentCategoryId());
            category.setParentCategory(parentCategory);
        } else {
            category.setParentCategory(null);
        }
    }
}
