package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.ReviewDto;
import org.example.enums.Role;
import org.example.exceptions.ReviewNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.models.User;
import org.example.services.interfaces.ReviewService;
import org.example.services.interfaces.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReviewController reviewController;

    private ObjectMapper objectMapper;

    private ReviewDto reviewDto;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Настраиваем MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

        // Создаем пример ReviewDto
        reviewDto = new ReviewDto();
        reviewDto.setId(1L);
        reviewDto.setUserId(1L);
        reviewDto.setProductId(1L);
        reviewDto.setRating(5);
        reviewDto.setUserComment("Great product!");

        // Создаем тестового пользователя
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setRole(Role.USER);
    }

    @AfterEach
    void tearDown() {
        // Очищаем SecurityContext после каждого теста
        SecurityContextHolder.clearContext();
    }

    private void setAuthentication(String email, Role role) {
        org.springframework.security.core.userdetails.User userPrincipal =
                new org.springframework.security.core.userdetails.User(email, "", Collections.singleton(() -> "ROLE_" + role.name()));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testGetAllReviews() throws Exception {
        setAuthentication("test@example.com", Role.USER);

        when(reviewService.getAllReviews()).thenReturn(Arrays.asList(reviewDto));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(reviewDto.getId().intValue())))
                .andExpect(jsonPath("$[0].userId", is(reviewDto.getUserId().intValue())))
                .andExpect(jsonPath("$[0].productId", is(reviewDto.getProductId().intValue())))
                .andExpect(jsonPath("$[0].rating", is(reviewDto.getRating())))
                .andExpect(jsonPath("$[0].userComment", is(reviewDto.getUserComment())));
    }

    @Test
    void testGetReviewById_Success() throws Exception {
        setAuthentication("test@example.com", Role.USER);

        when(reviewService.getReviewById(1L)).thenReturn(reviewDto);

        mockMvc.perform(get("/api/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(reviewDto.getId().intValue())))
                .andExpect(jsonPath("$.userId", is(reviewDto.getUserId().intValue())))
                .andExpect(jsonPath("$.productId", is(reviewDto.getProductId().intValue())))
                .andExpect(jsonPath("$.rating", is(reviewDto.getRating())))
                .andExpect(jsonPath("$.userComment", is(reviewDto.getUserComment())));
    }

    @Test
    void testGetReviewById_NotFound() throws Exception {
        setAuthentication("test@example.com", Role.USER);

        when(reviewService.getReviewById(1L)).thenThrow(new ReviewNotFoundException(ErrorMessage.REVIEW_NOT_FOUND));

        mockMvc.perform(get("/api/reviews/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.REVIEW_NOT_FOUND));
    }

    @Test
    void testCreateReview_Success() throws Exception {
        setAuthentication("test@example.com", Role.USER);

        when(reviewService.createReview(any(ReviewDto.class))).thenReturn(reviewDto);

        String reviewJson = objectMapper.writeValueAsString(reviewDto);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(reviewDto.getId().intValue())))
                .andExpect(jsonPath("$.userId", is(reviewDto.getUserId().intValue())))
                .andExpect(jsonPath("$.productId", is(reviewDto.getProductId().intValue())))
                .andExpect(jsonPath("$.rating", is(reviewDto.getRating())))
                .andExpect(jsonPath("$.userComment", is(reviewDto.getUserComment())));
    }

    @Test
    void testUpdateReview_Success() throws Exception {
        setAuthentication("test@example.com", Role.USER);

        ReviewDto updatedReviewDto = new ReviewDto();
        updatedReviewDto.setId(1L);
        updatedReviewDto.setUserId(1L);
        updatedReviewDto.setProductId(1L);
        updatedReviewDto.setRating(4);
        updatedReviewDto.setUserComment("Updated comment");

        when(reviewService.updateReview(eq(1L), any(ReviewDto.class))).thenReturn(updatedReviewDto);

        String reviewJson = objectMapper.writeValueAsString(updatedReviewDto);

        mockMvc.perform(put("/api/reviews/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedReviewDto.getId().intValue())))
                .andExpect(jsonPath("$.userId", is(updatedReviewDto.getUserId().intValue())))
                .andExpect(jsonPath("$.productId", is(updatedReviewDto.getProductId().intValue())))
                .andExpect(jsonPath("$.rating", is(updatedReviewDto.getRating())))
                .andExpect(jsonPath("$.userComment", is(updatedReviewDto.getUserComment())));
    }

    @Test
    void testDeleteReview_Success() throws Exception {
        setAuthentication("test@example.com", Role.USER);

        doNothing().when(reviewService).deleteReview(1L);

        mockMvc.perform(delete("/api/reviews/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetReviewsByProductId() throws Exception {
        setAuthentication("test@example.com", Role.USER);

        when(reviewService.getReviewsByProductId(1L)).thenReturn(Arrays.asList(reviewDto));

        mockMvc.perform(get("/api/reviews/product/{productId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(reviewDto.getId().intValue())))
                .andExpect(jsonPath("$[0].userId", is(reviewDto.getUserId().intValue())))
                .andExpect(jsonPath("$[0].productId", is(reviewDto.getProductId().intValue())))
                .andExpect(jsonPath("$[0].rating", is(reviewDto.getRating())))
                .andExpect(jsonPath("$[0].userComment", is(reviewDto.getUserComment())));
    }

    @Test
    void testGetReviewsByUserId() throws Exception {
        setAuthentication("test@example.com", Role.USER);

        when(reviewService.getReviewsByUserId(1L)).thenReturn(Arrays.asList(reviewDto));

        mockMvc.perform(get("/api/reviews/user/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(reviewDto.getId().intValue())))
                .andExpect(jsonPath("$[0].userId", is(reviewDto.getUserId().intValue())))
                .andExpect(jsonPath("$[0].productId", is(reviewDto.getProductId().intValue())))
                .andExpect(jsonPath("$[0].rating", is(reviewDto.getRating())))
                .andExpect(jsonPath("$[0].userComment", is(reviewDto.getUserComment())));
    }
}
