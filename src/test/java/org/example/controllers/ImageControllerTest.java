package org.example.controllers;

import jakarta.transaction.Transactional;
import org.example.enums.Role;
import org.example.models.Image;
import org.example.models.Product;
import org.example.models.User;
import org.example.repositories.ImageRepository;
import org.example.repositories.ProductRepository;
import org.example.repositories.UserRepository;
import org.example.services.impl.JwtSecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
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
public class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtSecurityService jwtSecurityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String adminToken;
    private User adminUser;
    private Product product;

    @BeforeEach
    public void setUp() {
        imageRepository.deleteAll();
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
    public void testUploadImageFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "Test Image Content".getBytes());

        mockMvc.perform(multipart("/images/upload/file/{productId}", product.getId())
                        .file(file)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url", notNullValue()))
                .andExpect(jsonPath("$.productId", is(product.getId().intValue())));
    }

    @Test
    public void testUploadImageUrl() throws Exception {
        String imageUrl = "http://example.com/test-image.jpg";

        mockMvc.perform(post("/images/upload/url/{productId}", product.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .content(imageUrl)
                        .contentType("text/plain"))
                .andExpect(status().isOk())
                // Проверяем, что возвращается URL сохраненного изображения
                .andExpect(jsonPath("$.url", containsString("/uploads/images/")))
                .andExpect(jsonPath("$.productId", is(product.getId().intValue())));
    }


    @Test
    public void testGetImagesByProductIdForUser() throws Exception {
        // Создание тестового изображения
        Image image = new Image();
        image.setProduct(product);
        image.setUrl("/uploads/images/test.jpg");
        image.setAltText("Test Image");
        image.setCreatedAt(LocalDateTime.now());
        image.setUpdatedAt(LocalDateTime.now());
        imageRepository.save(image);

        // Генерация токена обычного пользователя
        User regularUser = new User();
        regularUser.setEmail("user@example.com");
        regularUser.setPassword(passwordEncoder.encode("userpass"));
        regularUser.setRole(Role.USER);
        regularUser.setName("Regular User");
        regularUser.setCreatedAt(LocalDateTime.now());
        regularUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(regularUser);

        String userToken = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(regularUser.getEmail())
                        .password(regularUser.getPassword())
                        .roles(regularUser.getRole().name())
                        .build()
        );

        // Выполнение запроса от имени обычного пользователя
        mockMvc.perform(get("/images/product/{productId}", product.getId())
                        .header("Authorization", "Bearer " + userToken)) // Используем токен обычного пользователя
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].url", is("/uploads/images/test.jpg")))
                .andExpect(jsonPath("$[0].altText", is("Test Image")));
    }

    @Test
    public void testGetImagesByProductIdForGuest() throws Exception {
        // Создание тестового изображения
        Image image = new Image();
        image.setProduct(product);
        image.setUrl("/uploads/images/test.jpg");
        image.setAltText("Test Image");
        image.setCreatedAt(LocalDateTime.now());
        image.setUpdatedAt(LocalDateTime.now());
        imageRepository.save(image);

        // Выполнение запроса без авторизации
        mockMvc.perform(get("/images/product/{productId}", product.getId())) // Без токена
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].url", is("/uploads/images/test.jpg")))
                .andExpect(jsonPath("$[0].altText", is("Test Image")));
    }




    @Test
    public void testDeleteImage() throws Exception {
        Image image = new Image();
        image.setProduct(product);
        image.setUrl("/uploads/images/test.jpg");
        image.setAltText("Test Image");
        image.setCreatedAt(LocalDateTime.now());
        image.setUpdatedAt(LocalDateTime.now());
        image = imageRepository.save(image);

        mockMvc.perform(delete("/images/{imageId}", image.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }
}
