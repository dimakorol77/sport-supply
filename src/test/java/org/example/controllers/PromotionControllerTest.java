package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.dto.PromotionDto;
import org.example.enums.Role;
import org.example.models.Product;
import org.example.models.Promotion;
import org.example.models.User;
import org.example.repositories.ProductPromotionRepository;
import org.example.repositories.ProductRepository;
import org.example.repositories.PromotionRepository;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class PromotionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductPromotionRepository productPromotionRepository;

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
    private Promotion promotion;
    private Product product;

    @BeforeEach
    public void setUp() {
        productPromotionRepository.deleteAll();
        promotionRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();

        adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("adminpass"));
        adminUser.setRole(Role.ADMIN);
        adminUser.setName("Admin User");
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(adminUser);

        promotion = new Promotion();
        promotion.setName("Promotion1");
        promotion.setDescription("Promotion Description");
        promotion.setStartDate(LocalDateTime.now().minusDays(1));
        promotion.setEndDate(LocalDateTime.now().plusDays(1));
        promotionRepository.save(promotion);

        product = new Product();
        product.setName("Product1");
        product.setDescription("Product Description");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        adminToken = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(adminUser.getEmail())
                        .password(adminUser.getPassword())
                        .roles(adminUser.getRole().name())
                        .build());
    }

    @Test
    public void testGetAllPromotions() throws Exception {
        mockMvc.perform(get("/api/promotions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Promotion1")));
    }

    @Test
    public void testCreatePromotion() throws Exception {
        PromotionDto promotionDto = new PromotionDto();
        promotionDto.setName("New Promotion");
        promotionDto.setDescription("New Promotion Description");
        promotionDto.setStartDate(LocalDateTime.now());
        promotionDto.setEndDate(LocalDateTime.now().plusDays(5));

        mockMvc.perform(post("/api/promotions")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promotionDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New Promotion")));
    }

    @Test
    public void testUpdatePromotion() throws Exception {
        PromotionDto promotionDto = new PromotionDto();
        promotionDto.setName("Updated Promotion");
        promotionDto.setDescription("Updated Description");
        promotionDto.setStartDate(LocalDateTime.now());
        promotionDto.setEndDate(LocalDateTime.now().plusDays(10));

        mockMvc.perform(put("/api/promotions/{id}", promotion.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promotionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Promotion")))
                .andExpect(jsonPath("$.description", is("Updated Description")));
    }

    @Test
    public void testDeletePromotion() throws Exception {
        mockMvc.perform(delete("/api/promotions/{id}", promotion.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testAddProductToPromotion() throws Exception {
        mockMvc.perform(post("/api/promotions/{promotionId}/products/{productId}", promotion.getId(), product.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isCreated());

        // Перезагружаем объект Promotion из базы данных
        Promotion updatedPromotion = promotionRepository.findById(promotion.getId())
                .orElseThrow(() -> new RuntimeException("Promotion not found"));

        // Проверяем, что коллекция productPromotions не null и содержит 1 элемент
        assertNotNull(updatedPromotion.getProductPromotions(), "productPromotions should not be null");
        assertThat(updatedPromotion.getProductPromotions(), hasSize(1));
    }


    @Test
    public void testRemoveProductFromPromotion() throws Exception {
        // Сначала добавляем продукт к акции
        mockMvc.perform(post("/api/promotions/{promotionId}/products/{productId}", promotion.getId(), product.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isCreated());

        // Затем удаляем продукт из акции
        mockMvc.perform(delete("/api/promotions/{promotionId}/products/{productId}", promotion.getId(), product.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        // Перезагружаем объект Promotion из базы данных
        Promotion updatedPromotion = promotionRepository.findById(promotion.getId())
                .orElseThrow(() -> new RuntimeException("Promotion not found"));

        // Проверяем, что коллекция productPromotions не null и пуста
        assertNotNull(updatedPromotion.getProductPromotions(), "productPromotions should not be null");
        assertTrue(updatedPromotion.getProductPromotions().isEmpty());
    }

}

