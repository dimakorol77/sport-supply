package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.dto.UserDto;
import org.example.dto.UserLoginDto;
import org.example.enums.Role;
import org.example.models.User;
import org.example.repositories.UserRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("testuser@example.com");
        userDto.setPassword("password123");
        userDto.setName("Test User");
        userDto.setPhoneNumber("1234567890");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.email", is("testuser@example.com")))
                .andExpect(jsonPath("$.name", is("Test User")))
                .andExpect(jsonPath("$.phoneNumber", is("1234567890")));
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() throws Exception {
        User existingUser = new User();
        existingUser.setEmail("testuser@example.com");
        existingUser.setPassword(passwordEncoder.encode("password123"));
        existingUser.setName("Existing User");
        existingUser.setPhoneNumber("1234567890");
        existingUser.setCreatedAt(LocalDateTime.now());
        existingUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(existingUser);

        UserDto userDto = new UserDto();
        userDto.setEmail("testuser@example.com");
        userDto.setPassword("newpassword123");
        userDto.setName("New User");
        userDto.setPhoneNumber("0987654321");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("User with this email already exists")));
    }

    @Test
    public void testAuthenticateUser_Success() throws Exception {

        User user = new User();
        user.setEmail("loginuser@example.com");
        user.setPassword(passwordEncoder.encode("password123")); // Зашифрованный пароль
        user.setName("Login User");
        user.setPhoneNumber("1234567890");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole(Role.USER); // Устанавливаем роль пользователя
        userRepository.save(user);


        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setEmail("loginuser@example.com");
        loginDto.setPassword("password123");


        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.refreshToken", notNullValue()))
                .andExpect(jsonPath("$.tokenType", is("Bearer")));
    }


    @Test
    public void testAuthenticateUser_InvalidCredentials() throws Exception {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setEmail("nonexistent@example.com");
        loginDto.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("Incorrect credentials")));
    }
}
