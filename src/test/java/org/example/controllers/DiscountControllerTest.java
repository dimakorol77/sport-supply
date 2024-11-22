package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.DiscountDto;
import org.example.exceptions.DiscountNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.services.interfaces.DiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DiscountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private DiscountController discountController;

    private ObjectMapper objectMapper;

    private DiscountDto discountDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(discountController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        discountDto = new DiscountDto();
        discountDto.setId(1L);
        discountDto.setProductId(1L);
        discountDto.setDiscountPrice(new BigDecimal("100.00"));
        discountDto.setStartDate(LocalDateTime.now().minusDays(1));
        discountDto.setEndDate(LocalDateTime.now().plusDays(10));

        // Устанавливаем SecurityContext с аутентифицированным пользователем с ролью ADMIN
        org.springframework.security.core.userdetails.User userPrincipal =
                new org.springframework.security.core.userdetails.User("admin@example.com", "",
                        Collections.singletonList(() -> "ROLE_ADMIN"));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testGetAllDiscounts() throws Exception {
        when(discountService.getAllDiscounts()).thenReturn(Arrays.asList(discountDto));

        mockMvc.perform(get("/api/discounts/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(discountDto.getId().intValue())))
                .andExpect(jsonPath("$[0].productId", is(discountDto.getProductId().intValue())))
                .andExpect(jsonPath("$[0].discountPrice", is(discountDto.getDiscountPrice().doubleValue())));
    }

    @Test
    void testGetDiscountById_Success() throws Exception {
        when(discountService.getDiscountById(1L)).thenReturn(discountDto);

        mockMvc.perform(get("/api/discounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(discountDto.getId().intValue())))
                .andExpect(jsonPath("$.productId", is(discountDto.getProductId().intValue())))
                .andExpect(jsonPath("$.discountPrice", is(discountDto.getDiscountPrice().doubleValue())));
    }

    @Test
    void testGetDiscountById_NotFound() throws Exception {
        when(discountService.getDiscountById(1L))
                .thenThrow(new DiscountNotFoundException(ErrorMessage.DISCOUNT_NOT_FOUND));

        mockMvc.perform(get("/api/discounts/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.DISCOUNT_NOT_FOUND));
    }

    @Test
    void testCreateDiscount_Success() throws Exception {
        when(discountService.createDiscount(any(DiscountDto.class))).thenReturn(discountDto);

        String discountJson = objectMapper.writeValueAsString(discountDto);

        mockMvc.perform(post("/api/discounts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(discountJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(discountDto.getId().intValue())))
                .andExpect(jsonPath("$.productId", is(discountDto.getProductId().intValue())))
                .andExpect(jsonPath("$.discountPrice", is(discountDto.getDiscountPrice().doubleValue())));
    }

    @Test
    void testUpdateDiscount_Success() throws Exception {
        DiscountDto updatedDiscountDto = new DiscountDto();
        updatedDiscountDto.setId(1L);
        updatedDiscountDto.setProductId(1L);
        updatedDiscountDto.setDiscountPrice(new BigDecimal("80.00"));
        updatedDiscountDto.setStartDate(LocalDateTime.now());
        updatedDiscountDto.setEndDate(LocalDateTime.now().plusDays(15));

        when(discountService.updateDiscount(eq(1L), any(DiscountDto.class))).thenReturn(updatedDiscountDto);

        String discountJson = objectMapper.writeValueAsString(updatedDiscountDto);

        mockMvc.perform(put("/api/discounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(discountJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedDiscountDto.getId().intValue())))
                .andExpect(jsonPath("$.productId", is(updatedDiscountDto.getProductId().intValue())))
                .andExpect(jsonPath("$.discountPrice", is(updatedDiscountDto.getDiscountPrice().doubleValue())));
    }

    @Test
    void testDeleteDiscount_Success() throws Exception {
        doNothing().when(discountService).deleteDiscount(1L);

        mockMvc.perform(delete("/api/discounts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetActiveDiscountsForProduct() throws Exception {
        when(discountService.getActiveDiscountsForProduct(1L)).thenReturn(Arrays.asList(discountDto));

        mockMvc.perform(get("/api/discounts/product/1/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(discountDto.getId().intValue())))
                .andExpect(jsonPath("$[0].productId", is(discountDto.getProductId().intValue())))
                .andExpect(jsonPath("$[0].discountPrice", is(discountDto.getDiscountPrice().doubleValue())));
    }

    @Test
    void testUpdateDiscount_NotFound() throws Exception {
        when(discountService.updateDiscount(eq(1L), any(DiscountDto.class)))
                .thenThrow(new DiscountNotFoundException(ErrorMessage.DISCOUNT_NOT_FOUND));

        String discountJson = objectMapper.writeValueAsString(discountDto);

        mockMvc.perform(put("/api/discounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(discountJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.DISCOUNT_NOT_FOUND));
    }

    @Test
    void testDeleteDiscount_NotFound() throws Exception {
        doThrow(new DiscountNotFoundException(ErrorMessage.DISCOUNT_NOT_FOUND))
                .when(discountService).deleteDiscount(1L);

        mockMvc.perform(delete("/api/discounts/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.DISCOUNT_NOT_FOUND));
    }
}
