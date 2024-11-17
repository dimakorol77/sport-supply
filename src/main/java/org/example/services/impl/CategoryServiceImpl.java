package org.example.services.impl;

import org.example.dto.CategoryDto;
import org.example.exception.CategoryAlreadyExistsException;
import org.example.exception.CategoryNotFoundException;
import org.example.exception.errorMessage.ErrorMessage;
import org.example.mappers.CategoryMapper;
import org.example.models.Category;
import org.example.repositories.CategoryRepository;
import org.example.services.interfaces.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new CategoryNotFoundException(ErrorMessage.CATEGORY_NOT_FOUND));
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        // Предполагаем, что категория уникальна по названию
        categoryRepository.findByName(categoryDto.getName()).ifPresent(category -> {
            throw new CategoryAlreadyExistsException(ErrorMessage.CATEGORY_ALREADY_EXISTS);
        });

        Category category = categoryMapper.toEntity(categoryDto);
        category.setCreatedAt(LocalDateTime.now());
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(ErrorMessage.CATEGORY_NOT_FOUND));

        categoryMapper.updateEntityFromDto(categoryDto, category);
        category.setUpdatedAt(LocalDateTime.now());
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(ErrorMessage.CATEGORY_NOT_FOUND);
        }
        categoryRepository.deleteById(id);
    }
}
