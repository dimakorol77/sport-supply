// src/test/java/org/example/controllers/PromotionControllerTest.java

package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.PromotionDto;
import org.example.exceptions.PromotionNotFoundException;
import org.example.exceptions.ProductAlreadyInPromotionException;
import org.example.exceptions.ProductNotFoundException;
import org.example.exceptions.ProductPromotionNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.services.interfaces.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PromotionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private PromotionController promotionController;

    private PromotionDto promotionDto;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(promotionController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        promotionDto = new PromotionDto();
        promotionDto.setId(1L);
        promotionDto.setName("Summer Sale");
        promotionDto.setDescription("Discount for summer");
    }

    @Test
    void testGetAllPromotions() throws Exception {
        when(promotionService.getAllPromotions()).thenReturn(Arrays.asList(promotionDto));

        mockMvc.perform(get("/api/promotions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(promotionDto.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(promotionDto.getName())))
                .andExpect(jsonPath("$[0].description", is(promotionDto.getDescription())));
    }

    @Test
    void testGetPromotionById_Success() throws Exception {
        when(promotionService.getPromotionById(1L)).thenReturn(promotionDto);

        mockMvc.perform(get("/api/promotions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(promotionDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(promotionDto.getName())))
                .andExpect(jsonPath("$.description", is(promotionDto.getDescription())));
    }

    @Test
    void testGetPromotionById_NotFound() throws Exception {
        when(promotionService.getPromotionById(1L)).thenThrow(new PromotionNotFoundException(ErrorMessage.PROMOTION_NOT_FOUND));

        mockMvc.perform(get("/api/promotions/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.PROMOTION_NOT_FOUND));
    }

    @Test
    void testCreatePromotion_Success() throws Exception {
        when(promotionService.createPromotion(any(PromotionDto.class))).thenReturn(promotionDto);

        String promotionJson = objectMapper.writeValueAsString(promotionDto);

        mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(promotionJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(promotionDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(promotionDto.getName())))
                .andExpect(jsonPath("$.description", is(promotionDto.getDescription())));
    }

    @Test
    void testCreatePromotion_AlreadyExists() throws Exception {
        when(promotionService.createPromotion(any(PromotionDto.class))).thenThrow(new ProductAlreadyInPromotionException(ErrorMessage.PRODUCT_ALREADY_IN_PROMOTION));

        String promotionJson = objectMapper.writeValueAsString(promotionDto);

        mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(promotionJson))
                .andExpect(status().isConflict())
                .andExpect(content().string(ErrorMessage.PRODUCT_ALREADY_IN_PROMOTION));
    }

    @Test
    void testUpdatePromotion_Success() throws Exception {
        PromotionDto updatedPromotionDto = new PromotionDto();
        updatedPromotionDto.setId(1L);
        updatedPromotionDto.setName("Winter Sale");
        updatedPromotionDto.setDescription("Discount for winter");

        when(promotionService.updatePromotion(eq(1L), any(PromotionDto.class))).thenReturn(updatedPromotionDto);

        String updatedPromotionJson = objectMapper.writeValueAsString(updatedPromotionDto);

        mockMvc.perform(put("/api/promotions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPromotionJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedPromotionDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(updatedPromotionDto.getName())))
                .andExpect(jsonPath("$.description", is(updatedPromotionDto.getDescription())));
    }

    @Test
    void testUpdatePromotion_NotFound() throws Exception {
        PromotionDto updatedPromotionDto = new PromotionDto();
        updatedPromotionDto.setId(1L);
        updatedPromotionDto.setName("Winter Sale");
        updatedPromotionDto.setDescription("Discount for winter");

        when(promotionService.updatePromotion(eq(1L), any(PromotionDto.class))).thenThrow(new PromotionNotFoundException(ErrorMessage.PROMOTION_NOT_FOUND));

        String updatedPromotionJson = objectMapper.writeValueAsString(updatedPromotionDto);

        mockMvc.perform(put("/api/promotions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPromotionJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.PROMOTION_NOT_FOUND));
    }

    @Test
    void testDeletePromotion_Success() throws Exception {
        doNothing().when(promotionService).deletePromotion(1L);

        mockMvc.perform(delete("/api/promotions/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeletePromotion_NotFound() throws Exception {
        doThrow(new PromotionNotFoundException(ErrorMessage.PROMOTION_NOT_FOUND)).when(promotionService).deletePromotion(1L);

        mockMvc.perform(delete("/api/promotions/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.PROMOTION_NOT_FOUND));
    }

    @Test
    void testAddProductToPromotion_Success() throws Exception {
        mockMvc.perform(post("/api/promotions/1/products/1"))
                .andExpect(status().isCreated());

        verify(promotionService, times(1)).addProductToPromotion(1L, 1L);
    }

    @Test
    void testAddProductToPromotion_AlreadyExists() throws Exception {
        doThrow(new ProductAlreadyInPromotionException(ErrorMessage.PRODUCT_ALREADY_IN_PROMOTION))
                .when(promotionService).addProductToPromotion(1L, 1L);

        mockMvc.perform(post("/api/promotions/1/products/1"))
                .andExpect(status().isConflict())
                .andExpect(content().string(ErrorMessage.PRODUCT_ALREADY_IN_PROMOTION));
    }

    @Test
    void testAddProductToPromotion_PromotionNotFound() throws Exception {
        doThrow(new PromotionNotFoundException(ErrorMessage.PROMOTION_NOT_FOUND))
                .when(promotionService).addProductToPromotion(1L, 1L);

        mockMvc.perform(post("/api/promotions/1/products/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.PROMOTION_NOT_FOUND));
    }

    @Test
    void testAddProductToPromotion_ProductNotFound() throws Exception {
        doThrow(new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND))
                .when(promotionService).addProductToPromotion(1L, 1L);

        mockMvc.perform(post("/api/promotions/1/products/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.PRODUCT_NOT_FOUND));
    }

    @Test
    void testRemoveProductFromPromotion_Success() throws Exception {
        mockMvc.perform(delete("/api/promotions/1/products/1"))
                .andExpect(status().isNoContent());

        verify(promotionService, times(1)).removeProductFromPromotion(1L, 1L);
    }

    @Test
    void testRemoveProductFromPromotion_NotFound() throws Exception {
        doThrow(new ProductPromotionNotFoundException(ErrorMessage.PRODUCT_PROMOTION_NOT_FOUND))
                .when(promotionService).removeProductFromPromotion(1L, 1L);

        mockMvc.perform(delete("/api/promotions/1/products/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.PRODUCT_PROMOTION_NOT_FOUND));
    }
}
