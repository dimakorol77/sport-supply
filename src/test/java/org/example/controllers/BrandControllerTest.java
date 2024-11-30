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
@ActiveProfiles("test")
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

        // Создаем администратора
        adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("adminpass"));
        adminUser.setRole(Role.ADMIN);
        adminUser.setName("Admin User");
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(adminUser);

        // Генерация токена администратора
        adminToken = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(adminUser.getEmail())
                        .password(adminUser.getPassword())
                        .roles(adminUser.getRole().name())
                        .build());
    }

    @Test
    public void testGetAllBrands() throws Exception {
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

        // Выполняем GET запрос без авторизации (для гостей)
        mockMvc.perform(get("/api/brands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Brand1")))
                .andExpect(jsonPath("$[1].name", is("Brand2")));
    }

    @Test
    public void testGetBrandById() throws Exception {
        // Создаем бренд
        Brand brand = new Brand();
        brand.setName("Brand1");
        brand.setDescription("Description1");
        brand.setCreatedAt(LocalDateTime.now());
        brand.setUpdatedAt(LocalDateTime.now());
        brand = brandRepository.save(brand);

        // Выполняем GET запрос без авторизации (для гостей)
        mockMvc.perform(get("/api/brands/{id}", brand.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(brand.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Brand1")))
                .andExpect(jsonPath("$.description", is("Description1")));
    }

    @Test
    public void testCreateBrand_Success() throws Exception {
        // Создаем DTO для бренда
        BrandDto brandDto = new BrandDto();
        brandDto.setName("NewBrand");
        brandDto.setDescription("NewDescription");

        // Выполняем POST запрос с токеном администратора
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
    public void testCreateBrand_Forbidden() throws Exception {
        // Создаем DTO для бренда
        BrandDto brandDto = new BrandDto();
        brandDto.setName("NewBrand");
        brandDto.setDescription("NewDescription");

        // Выполняем POST запрос без токена
        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateBrand_Success() throws Exception {
        // Создаем бренд
        Brand brand = new Brand();
        brand.setName("Brand1");
        brand.setDescription("Description1");
        brand.setCreatedAt(LocalDateTime.now());
        brand.setUpdatedAt(LocalDateTime.now());
        brand = brandRepository.save(brand);

        // Создаем DTO для обновления бренда
        BrandDto brandDto = new BrandDto();
        brandDto.setName("UpdatedBrand");
        brandDto.setDescription("UpdatedDescription");

        // Выполняем PUT запрос с токеном администратора
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
        // Создаем бренд
        Brand brand = new Brand();
        brand.setName("BrandToDelete");
        brand.setDescription("Description");
        brand.setCreatedAt(LocalDateTime.now());
        brand.setUpdatedAt(LocalDateTime.now());
        brand = brandRepository.save(brand);

        // Выполняем DELETE запрос с токеном администратора
        mockMvc.perform(delete("/api/brands/{id}", brand.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteBrand_Forbidden() throws Exception {
        // Создаем бренд
        Brand brand = new Brand();
        brand.setName("BrandToDelete");
        brand.setDescription("Description");
        brand.setCreatedAt(LocalDateTime.now());
        brand.setUpdatedAt(LocalDateTime.now());
        brand = brandRepository.save(brand);

        // Выполняем DELETE запрос без токена
        mockMvc.perform(delete("/api/brands/{id}", brand.getId()))
                .andExpect(status().isForbidden());
    }
}
