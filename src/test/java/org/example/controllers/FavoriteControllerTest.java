// src/test/java/org/example/controllers/FavoriteControllerTest.java

package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.ProductDto;
import org.example.exceptions.FavoriteAlreadyExistsException;
import org.example.exceptions.FavoriteNotFoundException;
import org.example.exceptions.ProductNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.models.User;
import org.example.services.interfaces.FavoriteService;
import org.example.services.interfaces.UserService;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FavoriteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FavoriteService favoriteService;

    @Mock
    private UserService userService;

    @InjectMocks
    private FavoriteController favoriteController;

    private ProductDto productDto;

    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(favoriteController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");

        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        // Устанавливаем аутентифицированного пользователя
        setAuthentication(user.getEmail());

        // Мокаем метод userService.getUserByEmail()
        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
    }

    @Test
    void testAddProductToFavorites_Success() throws Exception {
        doNothing().when(favoriteService).addProductToFavorites(user.getId(), 1L);

        mockMvc.perform(post("/api/favorites/add/{productId}", 1L))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddProductToFavorites_AlreadyExists() throws Exception {
        doThrow(new FavoriteAlreadyExistsException(ErrorMessage.FAVORITE_ALREADY_EXISTS))
                .when(favoriteService).addProductToFavorites(user.getId(), 1L);

        mockMvc.perform(post("/api/favorites/add/{productId}", 1L))
                .andExpect(status().isConflict())
                .andExpect(content().string(ErrorMessage.FAVORITE_ALREADY_EXISTS));
    }

    @Test
    void testGetUserFavorites() throws Exception {
        when(favoriteService.getUserFavorites(user.getId())).thenReturn(Arrays.asList(productDto));

        mockMvc.perform(get("/api/favorites/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(productDto.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(productDto.getName())))
                .andExpect(jsonPath("$[0].description", is(productDto.getDescription())));
    }

    @Test
    void testRemoveProductFromFavorites_Success() throws Exception {
        doNothing().when(favoriteService).removeProductFromFavorites(user.getId(), 1L);

        mockMvc.perform(delete("/api/favorites/remove/{productId}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testRemoveProductFromFavorites_NotFound() throws Exception {
        doThrow(new FavoriteNotFoundException(ErrorMessage.FAVORITE_NOT_FOUND))
                .when(favoriteService).removeProductFromFavorites(user.getId(), 1L);

        mockMvc.perform(delete("/api/favorites/remove/{productId}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.FAVORITE_NOT_FOUND));
    }

    // Метод для установки аутентификации
    private void setAuthentication(String email) {
        org.springframework.security.core.userdetails.User userPrincipal =
                new org.springframework.security.core.userdetails.User(email, "", Collections.emptyList());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
