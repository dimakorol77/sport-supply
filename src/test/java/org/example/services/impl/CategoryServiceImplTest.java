// src/test/java/org/example/services/impl/CategoryServiceImplTest.java

package org.example.services.impl;

import org.example.dto.CategoryDto;
import org.example.exceptions.CategoryAlreadyExistsException;
import org.example.exceptions.CategoryNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.CategoryMapper;
import org.example.models.Category;
import org.example.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        category.setDescription("Test Description");
        category.setCreatedAt(LocalDateTime.now());

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Test Category");
        categoryDto.setDescription("Test Description");
    }

    @Test
    void testGetAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        List<CategoryDto> categories = categoryService.getAllCategories();

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals(categoryDto, categories.get(0));
    }

    @Test
    void testGetCategoryById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(categoryDto, result);
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(1L));
        assertEquals(ErrorMessage.CATEGORY_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testCreateCategory_Success() {
        when(categoryRepository.findByName("Test Category")).thenReturn(Optional.empty());
        when(categoryMapper.toEntity(categoryDto)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.createCategory(categoryDto);

        assertNotNull(result);
        assertEquals(categoryDto, result);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testCreateCategory_AlreadyExists() {
        when(categoryRepository.findByName("Test Category")).thenReturn(Optional.of(category));

        CategoryAlreadyExistsException exception = assertThrows(CategoryAlreadyExistsException.class, () -> categoryService.createCategory(categoryDto));
        assertEquals(ErrorMessage.CATEGORY_ALREADY_EXISTS, exception.getMessage());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void testUpdateCategory_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper).updateEntityFromDto(categoryDto, category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.updateCategory(1L, categoryDto);

        assertNotNull(result);
        assertEquals(categoryDto, result);
    }

    @Test
    void testUpdateCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(1L, categoryDto));
        assertEquals(ErrorMessage.CATEGORY_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testDeleteCategory_Success() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> categoryService.deleteCategory(1L));
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(1L));
        assertEquals(ErrorMessage.CATEGORY_NOT_FOUND, exception.getMessage());
        verify(categoryRepository, never()).deleteById(anyLong());
    }
}
