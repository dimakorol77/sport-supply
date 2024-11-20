// src/test/java/org/example/services/impl/FavoriteServiceImplTest.java

package org.example.services.impl;

import org.example.dto.ProductDto;
import org.example.exceptions.FavoriteAlreadyExistsException;
import org.example.exceptions.FavoriteNotFoundException;
import org.example.exceptions.ProductNotFoundException;
import org.example.exceptions.UserNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.ProductMapper;
import org.example.models.Favorite;
import org.example.models.Product;
import org.example.models.User;
import org.example.repositories.FavoriteRepository;
import org.example.repositories.ProductRepository;
import org.example.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private User user;
    private Product product;
    private Favorite favorite;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");

        favorite = new Favorite();
        favorite.setId(1L);
        favorite.setUser(user);
        favorite.setProduct(product);
        favorite.setAddedAt(LocalDateTime.now());

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
    }

    @Test
    void testAddProductToFavorites_Success() {
        when(favoriteRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);

        assertDoesNotThrow(() -> favoriteService.addProductToFavorites(1L, 1L));
        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }

    @Test
    void testAddProductToFavorites_AlreadyExists() {
        when(favoriteRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(true);

        FavoriteAlreadyExistsException exception = assertThrows(FavoriteAlreadyExistsException.class, () -> favoriteService.addProductToFavorites(1L, 1L));
        assertEquals(ErrorMessage.FAVORITE_ALREADY_EXISTS, exception.getMessage());
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    void testAddProductToFavorites_UserNotFound() {
        when(favoriteRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> favoriteService.addProductToFavorites(1L, 1L));
        assertEquals(ErrorMessage.USER_NOT_FOUND, exception.getMessage());
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    void testAddProductToFavorites_ProductNotFound() {
        when(favoriteRepository.existsByUserIdAndProductId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> favoriteService.addProductToFavorites(1L, 1L));
        assertEquals(ErrorMessage.PRODUCT_NOT_FOUND, exception.getMessage());
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    void testGetUserFavorites() {
        when(favoriteRepository.findByUserId(1L)).thenReturn(Arrays.asList(favorite));
        when(productMapper.toDto(product)).thenReturn(productDto);

        List<ProductDto> favorites = favoriteService.getUserFavorites(1L);

        assertNotNull(favorites);
        assertEquals(1, favorites.size());
        assertEquals(productDto, favorites.get(0));
    }

    @Test
    void testRemoveProductFromFavorites_Success() {
        when(favoriteRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.of(favorite));

        assertDoesNotThrow(() -> favoriteService.removeProductFromFavorites(1L, 1L));
        verify(favoriteRepository, times(1)).delete(favorite);
    }

    @Test
    void testRemoveProductFromFavorites_NotFound() {
        when(favoriteRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.empty());

        FavoriteNotFoundException exception = assertThrows(FavoriteNotFoundException.class, () -> favoriteService.removeProductFromFavorites(1L, 1L));
        assertEquals(ErrorMessage.FAVORITE_NOT_FOUND, exception.getMessage());
        verify(favoriteRepository, never()).delete(any());
    }
}
