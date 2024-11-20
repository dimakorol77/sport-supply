// src/test/java/org/example/services/impl/PromotionServiceImplTest.java

package org.example.services.impl;

import org.example.dto.PromotionDto;
import org.example.exceptions.ProductAlreadyInPromotionException;
import org.example.exceptions.ProductNotFoundException;
import org.example.exceptions.PromotionNotFoundException;
import org.example.exceptions.ProductPromotionNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.PromotionMapper;
import org.example.models.Product;
import org.example.models.Promotion;
import org.example.models.ProductPromotion;
import org.example.repositories.ProductPromotionRepository;
import org.example.repositories.ProductRepository;
import org.example.repositories.PromotionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PromotionServiceImplTest {

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductPromotionRepository productPromotionRepository;

    @Mock
    private PromotionMapper promotionMapper;

    @InjectMocks
    private PromotionServiceImpl promotionService;

    private Promotion promotion;
    private PromotionDto promotionDto;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        promotion = new Promotion();
        promotion.setId(1L);
        promotion.setName("Summer Sale");
        promotion.setDescription("Discount for summer");
        promotion.setStartDate(LocalDateTime.now().minusDays(1));
        promotion.setEndDate(LocalDateTime.now().plusDays(10));
        promotion.setCreatedAt(LocalDateTime.now());

        promotionDto = new PromotionDto();
        promotionDto.setId(1L);
        promotionDto.setName("Summer Sale");
        promotionDto.setDescription("Discount for summer");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
    }

    @Test
    void testGetAllPromotions() {
        when(promotionRepository.findAll()).thenReturn(Arrays.asList(promotion));
        when(promotionMapper.toDto(promotion)).thenReturn(promotionDto);

        List<PromotionDto> promotions = promotionService.getAllPromotions();

        assertNotNull(promotions);
        assertEquals(1, promotions.size());
        assertEquals(promotionDto, promotions.get(0));
    }

    @Test
    void testGetPromotionById_Success() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));
        when(promotionMapper.toDto(promotion)).thenReturn(promotionDto);

        PromotionDto result = promotionService.getPromotionById(1L);

        assertNotNull(result);
        assertEquals(promotionDto, result);
    }

    @Test
    void testGetPromotionById_NotFound() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.empty());

        PromotionNotFoundException exception = assertThrows(PromotionNotFoundException.class, () -> promotionService.getPromotionById(1L));
        assertEquals(ErrorMessage.PROMOTION_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testCreatePromotion_Success() {
        when(promotionMapper.toEntity(promotionDto)).thenReturn(promotion);
        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);
        when(promotionMapper.toDto(promotion)).thenReturn(promotionDto);

        PromotionDto result = promotionService.createPromotion(promotionDto);

        assertNotNull(result);
        assertEquals(promotionDto, result);
        verify(promotionRepository, times(1)).save(promotion);
    }

    @Test
    void testUpdatePromotion_Success() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));
        doNothing().when(promotionMapper).updateEntityFromDto(promotionDto, promotion);
        when(promotionRepository.save(promotion)).thenReturn(promotion);
        when(promotionMapper.toDto(promotion)).thenReturn(promotionDto);

        PromotionDto result = promotionService.updatePromotion(1L, promotionDto);

        assertNotNull(result);
        assertEquals(promotionDto, result);
    }

    @Test
    void testUpdatePromotion_NotFound() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.empty());

        PromotionNotFoundException exception = assertThrows(PromotionNotFoundException.class, () -> promotionService.updatePromotion(1L, promotionDto));
        assertEquals(ErrorMessage.PROMOTION_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testDeletePromotion_Success() {
        when(promotionRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> promotionService.deletePromotion(1L));
        verify(promotionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePromotion_NotFound() {
        when(promotionRepository.existsById(1L)).thenReturn(false);

        PromotionNotFoundException exception = assertThrows(PromotionNotFoundException.class, () -> promotionService.deletePromotion(1L));
        assertEquals(ErrorMessage.PROMOTION_NOT_FOUND, exception.getMessage());
        verify(promotionRepository, never()).deleteById(anyLong());
    }

    @Test
    void testAddProductToPromotion_Success() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productPromotionRepository.findByProductIdAndPromotionId(1L, 1L)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> promotionService.addProductToPromotion(1L, 1L));
        verify(productPromotionRepository, times(1)).save(any(ProductPromotion.class));
    }

    @Test
    void testAddProductToPromotion_AlreadyExists() {
        ProductPromotion existingPP = new ProductPromotion();
        existingPP.setProduct(product);
        existingPP.setPromotion(promotion);

        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productPromotionRepository.findByProductIdAndPromotionId(1L, 1L)).thenReturn(Optional.of(existingPP));

        ProductAlreadyInPromotionException exception = assertThrows(ProductAlreadyInPromotionException.class, () -> promotionService.addProductToPromotion(1L, 1L));
        assertEquals(ErrorMessage.PRODUCT_ALREADY_IN_PROMOTION, exception.getMessage());
        verify(productPromotionRepository, never()).save(any());
    }

    @Test
    void testAddProductToPromotion_PromotionNotFound() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.empty());

        PromotionNotFoundException exception = assertThrows(PromotionNotFoundException.class, () -> promotionService.addProductToPromotion(1L, 1L));
        assertEquals(ErrorMessage.PROMOTION_NOT_FOUND, exception.getMessage());
        verify(productPromotionRepository, never()).save(any());
    }

    @Test
    void testAddProductToPromotion_ProductNotFound() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotion));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> promotionService.addProductToPromotion(1L, 1L));
        assertEquals(ErrorMessage.PRODUCT_NOT_FOUND, exception.getMessage());
        verify(productPromotionRepository, never()).save(any());
    }

    @Test
    void testRemoveProductFromPromotion_Success() {
        ProductPromotion pp = new ProductPromotion();
        pp.setProduct(product);
        pp.setPromotion(promotion);

        when(productPromotionRepository.findByProductIdAndPromotionId(1L, 1L)).thenReturn(Optional.of(pp));

        assertDoesNotThrow(() -> promotionService.removeProductFromPromotion(1L, 1L));
        verify(productPromotionRepository, times(1)).delete(pp);
    }

    @Test
    void testRemoveProductFromPromotion_NotFound() {
        when(productPromotionRepository.findByProductIdAndPromotionId(1L, 1L)).thenReturn(Optional.empty());

        ProductPromotionNotFoundException exception = assertThrows(ProductPromotionNotFoundException.class, () -> promotionService.removeProductFromPromotion(1L, 1L));
        assertEquals(ErrorMessage.PRODUCT_PROMOTION_NOT_FOUND, exception.getMessage());
        verify(productPromotionRepository, never()).delete(any());
    }

    @Test
    void testGetPromotionsForProduct() {
        ProductPromotion pp = new ProductPromotion();
        pp.setProduct(product);
        pp.setPromotion(promotion);

        when(productPromotionRepository.findByProductId(1L)).thenReturn(Arrays.asList(pp));
        when(promotionMapper.toDto(promotion)).thenReturn(promotionDto);

        List<PromotionDto> promotions = promotionService.getPromotionsForProduct(1L);

        assertNotNull(promotions);
        assertEquals(1, promotions.size());
        assertEquals(promotionDto, promotions.get(0));
    }
}
