package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.BrandDto;
import org.example.enums.Role;
import org.example.models.Brand;
import org.example.models.User;
import org.example.repositories.BrandRepository;
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
@ActiveProfiles("test") // Указывает, что используется application-test.properties
public class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrandRepository brandRepository;

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
        brandRepository.deleteAll();
        userRepository.deleteAll();

        adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("adminpass"));
        adminUser.setRole(Role.ADMIN);
        adminUser.setName("Admin User");
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(adminUser);

        adminToken = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(adminUser.getEmail())
                        .password(adminUser.getPassword())
                        .roles(adminUser.getRole().name())
                        .build());
    }

    @Test
    public void testGetAllBrands() throws Exception {
        // Создаем уникального администратора
        User adminUser = new User();
        adminUser.setEmail("admin123@example.com"); // Уникальный email
        adminUser.setPassword(passwordEncoder.encode("adminpassword"));
        adminUser.setName("Admin User");
        adminUser.setRole(Role.ADMIN);
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(adminUser);

        // Генерация JWT токена
        String token = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(adminUser.getEmail())
                        .password(adminUser.getPassword())
                        .roles(adminUser.getRole().name())
                        .build()
        );

        // Создаем бренды
        Brand brand1 = new Brand();
        brand1.setName("Brand1");
        brand1.setDescription("Description1");
        brand1.setCreatedAt(LocalDateTime.now());
        brand1.setUpdatedAt(LocalDateTime.now());
        brandRepository.save(brand1);

        Brand brand2 = new Brand();
        brand2.setName("Brand2");
        brand2.setDescription("Description2");
        brand2.setCreatedAt(LocalDateTime.now());
        brand2.setUpdatedAt(LocalDateTime.now());
        brandRepository.save(brand2);

        // Выполняем GET запрос
        mockMvc.perform(get("/api/brands")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }



    @Test
    public void testGetBrandById() throws Exception {
        // Создание уникального пользователя-администратора
        User adminUser = new User();
        adminUser.setEmail("admin" + System.currentTimeMillis() + "@example.com");
        adminUser.setPassword(passwordEncoder.encode("adminpassword"));
        adminUser.setName("Admin User");
        adminUser.setRole(Role.ADMIN);
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(adminUser);

        // Генерация JWT токена
        String token = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(adminUser.getEmail())
                        .password(adminUser.getPassword())
                        .roles(adminUser.getRole().name())
                        .build()
        );

        // Создание бренда
        Brand brand = new Brand();
        brand.setName("Brand1");
        brand.setDescription("Description1");
        brand.setCreatedAt(LocalDateTime.now());
        brand.setUpdatedAt(LocalDateTime.now());
        brand = brandRepository.save(brand);

        // Выполнение GET-запроса с токеном
        mockMvc.perform(get("/api/brands/{id}", brand.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(brand.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Brand1")))
                .andExpect(jsonPath("$.description", is("Description1")));
    }



    @Test
    public void testCreateBrand_Success() throws Exception {
        BrandDto brandDto = new BrandDto();
        brandDto.setName("NewBrand");
        brandDto.setDescription("NewDescription");

        mockMvc.perform(post("/api/brands")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("NewBrand")))
                .andExpect(jsonPath("$.description", is("NewDescription")));
    }

    @Test
    public void testCreateBrand_Unauthorized() throws Exception {
        BrandDto brandDto = new BrandDto();
        brandDto.setName("NewBrand");
        brandDto.setDescription("NewDescription");

        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateBrand_Success() throws Exception {
        Brand brand = new Brand();
        brand.setName("Brand1");
        brand.setDescription("Description1");
        brand.setCreatedAt(LocalDateTime.now());
        brand.setUpdatedAt(LocalDateTime.now());
        brand = brandRepository.save(brand);

        BrandDto brandDto = new BrandDto();
        brandDto.setName("UpdatedBrand");
        brandDto.setDescription("UpdatedDescription");

        mockMvc.perform(put("/api/brands/{id}", brand.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(brand.getId().intValue())))
                .andExpect(jsonPath("$.name", is("UpdatedBrand")))
                .andExpect(jsonPath("$.description", is("UpdatedDescription")));
    }

    @Test
    public void testDeleteBrand_Success() throws Exception {
        Brand brand = new Brand();
        brand.setName("BrandToDelete");
        brand.setDescription("Description");
        brand.setCreatedAt(LocalDateTime.now());
        brand.setUpdatedAt(LocalDateTime.now());
        brand = brandRepository.save(brand);

        mockMvc.perform(delete("/api/brands/{id}", brand.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }
}
