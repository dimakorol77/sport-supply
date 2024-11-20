// src/test/java/org/example/controllers/CategoryControllerTest.java

package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.CategoryDto;
import org.example.exceptions.CategoryAlreadyExistsException;
import org.example.exceptions.CategoryNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.services.interfaces.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryDto categoryDto;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Test Category");
        categoryDto.setDescription("Test Description");
    }

    @Test
    void testGetAllCategories() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(categoryDto));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(categoryDto.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(categoryDto.getName())))
                .andExpect(jsonPath("$[0].description", is(categoryDto.getDescription())));
    }

    @Test
    void testGetCategoryById_Success() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(categoryDto);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(categoryDto.getName())))
                .andExpect(jsonPath("$.description", is(categoryDto.getDescription())));
    }

    @Test
    void testGetCategoryById_NotFound() throws Exception {
        when(categoryService.getCategoryById(1L)).thenThrow(new CategoryNotFoundException(ErrorMessage.CATEGORY_NOT_FOUND));

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.CATEGORY_NOT_FOUND));
    }

    @Test
    void testCreateCategory_Success() throws Exception {
        when(categoryService.createCategory(any(CategoryDto.class))).thenReturn(categoryDto);

        String categoryJson = objectMapper.writeValueAsString(categoryDto);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(categoryDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(categoryDto.getName())))
                .andExpect(jsonPath("$.description", is(categoryDto.getDescription())));
    }

    @Test
    void testCreateCategory_AlreadyExists() throws Exception {
        when(categoryService.createCategory(any(CategoryDto.class))).thenThrow(new CategoryAlreadyExistsException(ErrorMessage.CATEGORY_ALREADY_EXISTS));

        String categoryJson = objectMapper.writeValueAsString(categoryDto);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isConflict())
                .andExpect(content().string(ErrorMessage.CATEGORY_ALREADY_EXISTS));
    }

    @Test
    void testUpdateCategory_Success() throws Exception {
        CategoryDto updatedCategoryDto = new CategoryDto();
        updatedCategoryDto.setId(1L);
        updatedCategoryDto.setName("Updated Category");
        updatedCategoryDto.setDescription("Updated Description");

        when(categoryService.updateCategory(eq(1L), any(CategoryDto.class))).thenReturn(updatedCategoryDto);

        String updatedCategoryJson = objectMapper.writeValueAsString(updatedCategoryDto);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedCategoryJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedCategoryDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(updatedCategoryDto.getName())))
                .andExpect(jsonPath("$.description", is(updatedCategoryDto.getDescription())));
    }

    @Test
    void testUpdateCategory_NotFound() throws Exception {
        CategoryDto updatedCategoryDto = new CategoryDto();
        updatedCategoryDto.setId(1L);
        updatedCategoryDto.setName("Updated Category");
        updatedCategoryDto.setDescription("Updated Description");

        when(categoryService.updateCategory(eq(1L), any(CategoryDto.class))).thenThrow(new CategoryNotFoundException(ErrorMessage.CATEGORY_NOT_FOUND));

        String updatedCategoryJson = objectMapper.writeValueAsString(updatedCategoryDto);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedCategoryJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.CATEGORY_NOT_FOUND));
    }

    @Test
    void testDeleteCategory_Success() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteCategory_NotFound() throws Exception {
        doThrow(new CategoryNotFoundException(ErrorMessage.CATEGORY_NOT_FOUND)).when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.CATEGORY_NOT_FOUND));
    }
}
