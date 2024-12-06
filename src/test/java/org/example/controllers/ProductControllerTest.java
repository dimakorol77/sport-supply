package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ProductDto;
import org.example.enums.Role;
import org.example.models.Brand;
import org.example.models.Category;
import org.example.models.Product;
import org.example.models.User;
import org.example.repositories.BrandRepository;
import org.example.repositories.CategoryRepository;
import org.example.repositories.ProductRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

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
    private Brand brand;
    private Category category;

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        brandRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("adminpass"));
        adminUser.setRole(Role.ADMIN);
        adminUser.setName("Admin User");
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(adminUser);

        brand = new Brand();
        brand.setName("Brand1");
        brand.setDescription("Brand Description");
        brand.setCreatedAt(LocalDateTime.now());
        brand.setUpdatedAt(LocalDateTime.now());
        brandRepository.save(brand);

        category = new Category();
        category.setName("Category1");
        category.setDescription("Category Description");
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category);

        adminToken = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(adminUser.getEmail())
                        .password(adminUser.getPassword())
                        .roles(adminUser.getRole().name())
                        .build());
    }

    @Test
    public void testCreateProduct() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setName("Product1");
        productDto.setDescription("Product Description");
        productDto.setPrice(BigDecimal.valueOf(100.0));
        productDto.setBrandId(brand.getId());
        productDto.setCategoryId(category.getId());

        mockMvc.perform(post("/api/products")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Product1")))
                .andExpect(jsonPath("$.price", is(100.0)));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        Product product = new Product();
        product.setName("Product1");
        product.setDescription("Description1");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setBrand(brand);
        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Product1")));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = new Product();
        product.setName("Product1");
        product.setDescription("Description1");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setBrand(brand);
        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        ProductDto productDto = new ProductDto();
        productDto.setName("UpdatedProduct");
        productDto.setDescription("Updated Description");
        productDto.setPrice(BigDecimal.valueOf(150.0));

        mockMvc.perform(put("/api/products/{id}", product.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("UpdatedProduct")))
                .andExpect(jsonPath("$.price", is(150.0)));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Product product = new Product();
        product.setName("ProductToDelete");
        product.setDescription("Description");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setBrand(brand);
        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product = productRepository.save(product);

        mockMvc.perform(delete("/api/products/{id}", product.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }
}
