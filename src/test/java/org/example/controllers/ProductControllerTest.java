// src/test/java/org/example/controllers/ProductControllerTest.java

package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.ProductDto;
import org.example.exceptions.ProductAlreadyExistsException;
import org.example.exceptions.ProductNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.services.interfaces.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductDto productDto;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
        productDto.setPrice(new BigDecimal("99.99"));
    }

    @Test
    void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(Arrays.asList(productDto));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(productDto.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(productDto.getName())))
                .andExpect(jsonPath("$[0].description", is(productDto.getDescription())))
                .andExpect(jsonPath("$[0].price", is(99.99)));
    }

    @Test
    void testGetProductById_Success() throws Exception {
        when(productService.getProductById(1L)).thenReturn(productDto);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(productDto.getName())))
                .andExpect(jsonPath("$.description", is(productDto.getDescription())))
                .andExpect(jsonPath("$.price", is(99.99)));
    }

    @Test
    void testGetProductById_NotFound() throws Exception {
        when(productService.getProductById(1L)).thenThrow(new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.PRODUCT_NOT_FOUND));
    }

    @Test
    void testCreateProduct_Success() throws Exception {
        when(productService.createProduct(any(ProductDto.class))).thenReturn(productDto);

        String productJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(productDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(productDto.getName())))
                .andExpect(jsonPath("$.description", is(productDto.getDescription())))
                .andExpect(jsonPath("$.price", is(99.99)));
    }

    @Test
    void testCreateProduct_AlreadyExists() throws Exception {
        when(productService.createProduct(any(ProductDto.class))).thenThrow(new ProductAlreadyExistsException(ErrorMessage.PRODUCT_ALREADY_EXISTS));

        String productJson = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isConflict())
                .andExpect(content().string(ErrorMessage.PRODUCT_ALREADY_EXISTS));
    }

    @Test
    void testUpdateProduct_Success() throws Exception {
        ProductDto updatedProductDto = new ProductDto();
        updatedProductDto.setId(1L);
        updatedProductDto.setName("Updated Product");
        updatedProductDto.setDescription("Updated Description");
        updatedProductDto.setPrice(new BigDecimal("149.99"));

        when(productService.updateProduct(eq(1L), any(ProductDto.class))).thenReturn(updatedProductDto);

        String updatedProductJson = objectMapper.writeValueAsString(updatedProductDto);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedProductDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(updatedProductDto.getName())))
                .andExpect(jsonPath("$.description", is(updatedProductDto.getDescription())))
                .andExpect(jsonPath("$.price", is(149.99)));
    }

    @Test
    void testUpdateProduct_NotFound() throws Exception {
        ProductDto updatedProductDto = new ProductDto();
        updatedProductDto.setId(1L);
        updatedProductDto.setName("Updated Product");
        updatedProductDto.setDescription("Updated Description");
        updatedProductDto.setPrice(new BigDecimal("149.99"));

        when(productService.updateProduct(eq(1L), any(ProductDto.class))).thenThrow(new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));

        String updatedProductJson = objectMapper.writeValueAsString(updatedProductDto);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.PRODUCT_NOT_FOUND));
    }

    @Test
    void testDeleteProduct_Success() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProduct_NotFound() throws Exception {
        doThrow(new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND)).when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.PRODUCT_NOT_FOUND));
    }
}
