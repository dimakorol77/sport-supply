package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.DiscountDto;
import org.example.enums.Role;
import org.example.models.Discount;
import org.example.models.Product;
import org.example.models.User;
import org.example.repositories.DiscountRepository;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DiscountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ProductRepository productRepository;

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
    private Product product;

    @BeforeEach
    public void setUp() {
        discountRepository.deleteAll();
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

        product = new Product();
        product.setName("Product1");
        product.setDescription("Description1");
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
    public void testGetAllDiscounts() throws Exception {
        Discount discount1 = new Discount();
        discount1.setProduct(product);
        discount1.setDiscountPrice(BigDecimal.valueOf(10.0));
        discount1.setStartDate(LocalDateTime.now().minusDays(1));
        discount1.setEndDate(LocalDateTime.now().plusDays(1));
        discount1.setCreatedAt(LocalDateTime.now());
        discount1.setUpdatedAt(LocalDateTime.now());
        discountRepository.save(discount1);

        mockMvc.perform(get("/api/discounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testCreateDiscount_Success() throws Exception {
        DiscountDto discountDto = new DiscountDto();
        discountDto.setProductId(product.getId());
        discountDto.setDiscountPrice(BigDecimal.valueOf(10.0));
        discountDto.setStartDate(LocalDateTime.now().minusDays(1));
        discountDto.setEndDate(LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/api/discounts")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discountDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.productId", is(product.getId().intValue())))
                .andExpect(jsonPath("$.discountPrice", is(10.0)));
    }

    @Test
    public void testUpdateDiscount_Success() throws Exception {
        Discount discount = new Discount();
        discount.setProduct(product);
        discount.setDiscountPrice(BigDecimal.valueOf(10.0));
        discount.setStartDate(LocalDateTime.now().minusDays(1));
        discount.setEndDate(LocalDateTime.now().plusDays(1));
        discount.setCreatedAt(LocalDateTime.now());
        discount.setUpdatedAt(LocalDateTime.now());
        discount = discountRepository.save(discount);

        DiscountDto discountDto = new DiscountDto();
        discountDto.setProductId(product.getId());
        discountDto.setDiscountPrice(BigDecimal.valueOf(15.0));
        discountDto.setStartDate(LocalDateTime.now());
        discountDto.setEndDate(LocalDateTime.now().plusDays(2));

        mockMvc.perform(put("/api/discounts/{id}", discount.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discountDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(discount.getId().intValue())))
                .andExpect(jsonPath("$.discountPrice", is(15.0)));
    }

    @Test
    public void testDeleteDiscount_Success() throws Exception {
        Discount discount = new Discount();
        discount.setProduct(product);
        discount.setDiscountPrice(BigDecimal.valueOf(10.0));
        discount.setStartDate(LocalDateTime.now().minusDays(1));
        discount.setEndDate(LocalDateTime.now().plusDays(1));
        discount.setCreatedAt(LocalDateTime.now());
        discount.setUpdatedAt(LocalDateTime.now());
        discount = discountRepository.save(discount);

        mockMvc.perform(delete("/api/discounts/{id}", discount.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }
}
