// src/test/java/org/example/controllers/FavoriteControllerTest.java

package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.ProductDto;
import org.example.exceptions.FavoriteAlreadyExistsException;
import org.example.exceptions.FavoriteNotFoundException;
import org.example.exceptions.ProductNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.services.interfaces.FavoriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FavoriteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FavoriteService favoriteService;

    @InjectMocks
    private FavoriteController favoriteController;

    private ProductDto productDto;

    private ObjectMapper objectMapper;

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
    }

    @Test
    void testAddProductToFavorites_Success() throws Exception {
        doNothing().when(favoriteService).addProductToFavorites(1L, 1L);

        mockMvc.perform(post("/api/users/1/favorites/1"))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddProductToFavorites_AlreadyExists() throws Exception {
        doThrow(new FavoriteAlreadyExistsException(ErrorMessage.FAVORITE_ALREADY_EXISTS))
                .when(favoriteService).addProductToFavorites(1L, 1L);

        mockMvc.perform(post("/api/users/1/favorites/1"))
                .andExpect(status().isConflict())
                .andExpect(content().string(ErrorMessage.FAVORITE_ALREADY_EXISTS));
    }

    @Test
    void testGetUserFavorites() throws Exception {
        when(favoriteService.getUserFavorites(1L)).thenReturn(Arrays.asList(productDto));

        mockMvc.perform(get("/api/users/1/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(productDto.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(productDto.getName())))
                .andExpect(jsonPath("$[0].description", is(productDto.getDescription())));
    }

    @Test
    void testRemoveProductFromFavorites_Success() throws Exception {
        doNothing().when(favoriteService).removeProductFromFavorites(1L, 1L);

        mockMvc.perform(delete("/api/users/1/favorites/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testRemoveProductFromFavorites_NotFound() throws Exception {
        doThrow(new FavoriteNotFoundException(ErrorMessage.FAVORITE_NOT_FOUND))
                .when(favoriteService).removeProductFromFavorites(1L, 1L);

        mockMvc.perform(delete("/api/users/1/favorites/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.FAVORITE_NOT_FOUND));
    }
}
