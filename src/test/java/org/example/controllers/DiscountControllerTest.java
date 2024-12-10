package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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
@Transactional
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
    private String userToken;
    private User adminUser;
    private User regularUser;
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

        adminToken = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(adminUser.getEmail())
                        .password(adminUser.getPassword())
                        .roles(adminUser.getRole().name())
                        .build()
        );


        regularUser = new User();
        regularUser.setEmail("user@example.com");
        regularUser.setPassword(passwordEncoder.encode("userpass"));
        regularUser.setRole(Role.USER);
        regularUser.setName("Regular User");
        regularUser.setCreatedAt(LocalDateTime.now());
        regularUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(regularUser);

        userToken = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(regularUser.getEmail())
                        .password(regularUser.getPassword())
                        .roles(regularUser.getRole().name())
                        .build()
        );


        product = new Product();
        product.setName("Product1");
        product.setDescription("Description1");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    @Test
    public void testGetAllDiscounts_WithoutAuth() throws Exception {
        Discount discount = new Discount();
        discount.setProduct(product);
        discount.setDiscountPrice(BigDecimal.valueOf(10.0));
        discount.setStartDate(LocalDateTime.now().minusDays(1));
        discount.setEndDate(LocalDateTime.now().plusDays(1));
        discount.setCreatedAt(LocalDateTime.now());
        discount.setUpdatedAt(LocalDateTime.now());
        discountRepository.save(discount);

        // GET request without authorization
        mockMvc.perform(get("/api/discounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testCreateDiscount_Unauthorized() throws Exception {
        DiscountDto discountDto = new DiscountDto();
        discountDto.setProductId(product.getId());
        discountDto.setDiscountPrice(BigDecimal.valueOf(10.0));
        discountDto.setStartDate(LocalDateTime.now().minusDays(1));
        discountDto.setEndDate(LocalDateTime.now().plusDays(1));

        // POST request from a user without the ADMIN role
        mockMvc.perform(post("/api/discounts")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discountDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateDiscount_Unauthorized() throws Exception {
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

        // PUT request from a user without the ADMIN role
        mockMvc.perform(put("/api/discounts/{id}", discount.getId())
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discountDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteDiscount_Unauthorized() throws Exception {
        Discount discount = new Discount();
        discount.setProduct(product);
        discount.setDiscountPrice(BigDecimal.valueOf(10.0));
        discount.setStartDate(LocalDateTime.now().minusDays(1));
        discount.setEndDate(LocalDateTime.now().plusDays(1));
        discount.setCreatedAt(LocalDateTime.now());
        discount.setUpdatedAt(LocalDateTime.now());
        discount = discountRepository.save(discount);

        // DELETE request from a user without the ADMIN role
        mockMvc.perform(delete("/api/discounts/{id}", discount.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }
    @Test
    public void testCreateDiscount_AdminAuthorized() throws Exception {
        DiscountDto discountDto = new DiscountDto();
        discountDto.setProductId(product.getId());
        discountDto.setDiscountPrice(BigDecimal.valueOf(10.0));
        discountDto.setStartDate(LocalDateTime.now().minusDays(1));
        discountDto.setEndDate(LocalDateTime.now().plusDays(1));

        // POST request from an administrator
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
    public void testUpdateDiscount_AdminAuthorized() throws Exception {
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

        // PUT request from an administrator
        mockMvc.perform(put("/api/discounts/{id}", discount.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discountDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(discount.getId().intValue())))
                .andExpect(jsonPath("$.discountPrice", is(15.0)));
    }

    @Test
    public void testDeleteDiscount_AdminAuthorized() throws Exception {
        Discount discount = new Discount();
        discount.setProduct(product);
        discount.setDiscountPrice(BigDecimal.valueOf(10.0));
        discount.setStartDate(LocalDateTime.now().minusDays(1));
        discount.setEndDate(LocalDateTime.now().plusDays(1));
        discount.setCreatedAt(LocalDateTime.now());
        discount.setUpdatedAt(LocalDateTime.now());
        discount = discountRepository.save(discount);

        // DELETE request from an administrator
        mockMvc.perform(delete("/api/discounts/{id}", discount.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }

}
