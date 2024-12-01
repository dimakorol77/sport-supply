package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.CategoryDto;
import org.example.enums.Role;
import org.example.models.Category;
import org.example.models.User;
import org.example.repositories.CategoryRepository;
import org.example.repositories.UserRepository;
import org.example.services.impl.JwtSecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtSecurityService jwtSecurityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;
    private User adminUser;


    @BeforeEach
    public void setUp() {
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        // Создание администратора
        adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("adminpass"));
        adminUser.setRole(Role.ADMIN);
        adminUser.setName("Admin User");
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(adminUser);

        // Генерация токена для администратора
        adminToken = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(adminUser.getEmail())
                        .password(adminUser.getPassword())
                        .roles(adminUser.getRole().name())
                        .build()
        );
    }



    @Test
    public void testGetAllCategories() throws Exception {
        Category category1 = new Category();
        category1.setName("Category1");
        category1.setDescription("Description1");
        category1.setCreatedAt(LocalDateTime.now());
        category1.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("Category2");
        category2.setDescription("Description2");
        category2.setCreatedAt(LocalDateTime.now());
        category2.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category2);

        // Выполнение GET-запроса без авторизации
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Category1")))
                .andExpect(jsonPath("$[1].name", is("Category2")));
    }

    @Test
    public void testGetCategoryById() throws Exception {
        // Создание категории
        Category category = new Category();
        category.setName("Category1");
        category.setDescription("Description1");
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        category = categoryRepository.save(category);

        // Выполнение GET-запроса без авторизации
        mockMvc.perform(get("/api/categories/{id}", category.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(category.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Category1")))
                .andExpect(jsonPath("$.description", is("Description1")));
    }

    @Test
    public void testCreateCategory_Success() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("NewCategory");
        categoryDto.setDescription("NewDescription");

        mockMvc.perform(post("/api/categories")
                        .header("Authorization", "Bearer " + adminToken) // Админ-токен обязателен
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("NewCategory")))
                .andExpect(jsonPath("$.description", is("NewDescription")));
    }

    @Test
    public void testUpdateCategory_Success() throws Exception {
        Category category = new Category();
        category.setName("Category1");
        category.setDescription("Description1");
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        category = categoryRepository.save(category);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("UpdatedCategory");
        categoryDto.setDescription("UpdatedDescription");

        mockMvc.perform(put("/api/categories/{id}", category.getId())
                        .header("Authorization", "Bearer " + adminToken) // Админ-токен обязателен
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(category.getId().intValue())))
                .andExpect(jsonPath("$.name", is("UpdatedCategory")))
                .andExpect(jsonPath("$.description", is("UpdatedDescription")));
    }

    @Test
    public void testDeleteCategory_Success() throws Exception {
        Category category = new Category();
        category.setName("CategoryToDelete");
        category.setDescription("Description");
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        category = categoryRepository.save(category);

        mockMvc.perform(delete("/api/categories/{id}", category.getId())
                        .header("Authorization", "Bearer " + adminToken)) // Админ-токен обязателен
                .andExpect(status().isNoContent());
    }
}
