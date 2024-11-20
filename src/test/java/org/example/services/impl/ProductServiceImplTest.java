// src/test/java/org/example/services/impl/ProductServiceImplTest.java

package org.example.services.impl;

import org.example.dto.ProductDto;
import org.example.dto.PromotionDto;
import org.example.exceptions.ProductAlreadyExistsException;
import org.example.exceptions.ProductNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.ProductMapper;
import org.example.mappers.PromotionMapper;
import org.example.models.Product;
import org.example.models.Promotion;
import org.example.models.ProductPromotion;
import org.example.repositories.ProductPromotionRepository;
import org.example.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductPromotionRepository productPromotionRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private PromotionMapper promotionMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDto productDto;
    private Promotion promotion;
    private PromotionDto promotionDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("100.00"));
        product.setCreatedAt(LocalDateTime.now());

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
        productDto.setPrice(new BigDecimal("100.00"));

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
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));
        when(productMapper.toDto(product)).thenReturn(productDto);

        List<ProductDto> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(productDto, products.get(0));
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(productDto, result);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
        assertEquals(ErrorMessage.PRODUCT_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testCreateProduct_Success() {
        when(productRepository.findByName("Test Product")).thenReturn(Optional.empty());
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.createProduct(productDto);

        assertNotNull(result);
        assertEquals(productDto, result);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testCreateProduct_AlreadyExists() {
        when(productRepository.findByName("Test Product")).thenReturn(Optional.of(product));

        ProductAlreadyExistsException exception = assertThrows(ProductAlreadyExistsException.class, () -> productService.createProduct(productDto));
        assertEquals(ErrorMessage.PRODUCT_ALREADY_EXISTS, exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void testUpdateProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productMapper).updateEntityFromDto(productDto, product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.updateProduct(1L, productDto);

        assertNotNull(result);
        assertEquals(productDto, result);
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, productDto));
        assertEquals(ErrorMessage.PRODUCT_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> productService.deleteProduct(1L));
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
        assertEquals(ErrorMessage.PRODUCT_NOT_FOUND, exception.getMessage());
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetPromotionsForProduct() {
        ProductPromotion productPromotion = new ProductPromotion();
        productPromotion.setProduct(product);
        productPromotion.setPromotion(promotion);

        when(productPromotionRepository.findByProductId(1L)).thenReturn(Arrays.asList(productPromotion));
        when(promotionMapper.toDto(promotion)).thenReturn(promotionDto);

        List<PromotionDto> promotions = productService.getPromotionsForProduct(1L);

        assertNotNull(promotions);
        assertEquals(1, promotions.size());
        assertEquals(promotionDto, promotions.get(0));
    }
}
