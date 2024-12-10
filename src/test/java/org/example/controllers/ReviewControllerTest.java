package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ReviewDto;
import org.example.enums.Role;
import org.example.models.Product;
import org.example.models.Review;
import org.example.models.User;
import org.example.repositories.ProductRepository;
import org.example.repositories.ReviewRepository;
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
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewRepository reviewRepository;

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

    private String userToken;
    private String adminToken;
    private User user;
    private User adminUser;
    private Product product;
    private Review review;

    @BeforeEach
    public void setUp() {
        reviewRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("userpass"));
        user.setRole(Role.USER);
        user.setName("Test User");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

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
        product.setDescription("Product Description");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(4);
        review.setUserComment("Good product");
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepository.save(review);

        userToken = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build());

        adminToken = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(adminUser.getEmail())
                        .password(adminUser.getPassword())
                        .roles(adminUser.getRole().name())
                        .build());
    }

    @Test
    public void testGetAllReviews() throws Exception {
        mockMvc.perform(get("/api/reviews")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userComment", is("Good product")));
    }


    @Test
    public void testCreateReview() throws Exception {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setProductId(product.getId());
        reviewDto.setRating(5);
        reviewDto.setUserComment("Excellent!");

        mockMvc.perform(post("/api/reviews")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userComment", is("Excellent!")))
                .andExpect(jsonPath("$.rating", is(5)));
    }

    @Test
    public void testUpdateReview() throws Exception {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setProductId(product.getId());
        reviewDto.setRating(3);
        reviewDto.setUserComment("Updated comment");

        mockMvc.perform(put("/api/reviews/{id}", review.getId())
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userComment", is("Updated comment")))
                .andExpect(jsonPath("$.rating", is(3)));
    }



    @Test
    public void testDeleteReview() throws Exception {
        mockMvc.perform(delete("/api/reviews/{id}", review.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetReviewsByProductId() throws Exception {
        mockMvc.perform(get("/api/reviews/product/{productId}", product.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userComment", is("Good product")));
    }



    @Test
    public void testGetReviewsByUserId() throws Exception {
        mockMvc.perform(get("/api/reviews/user/{userId}", user.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userComment", is("Good product")));
    }

}
