// src/test/java/org/example/services/impl/DiscountServiceImplTest.java

package org.example.services.impl;

import org.example.dto.DiscountDto;
import org.example.exceptions.DiscountNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.DiscountMapper;
import org.example.models.Discount;
import org.example.repositories.DiscountRepository;
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

class DiscountServiceImplTest {

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private DiscountMapper discountMapper;

    @InjectMocks
    private DiscountServiceImpl discountService;

    private Discount discount;
    private DiscountDto discountDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        discount = new Discount();
        discount.setId(1L);
        discount.setDiscountPrice(new BigDecimal("10.00"));
        discount.setStartDate(LocalDateTime.now().minusDays(1));
        discount.setEndDate(LocalDateTime.now().plusDays(5));
        discount.setCreatedAt(LocalDateTime.now());

        discountDto = new DiscountDto();
        discountDto.setId(1L);
        discountDto.setDiscountPrice(new BigDecimal("10.00"));
        discountDto.setStartDate(LocalDateTime.now().minusDays(1));
        discountDto.setEndDate(LocalDateTime.now().plusDays(5));
    }

    @Test
    void testGetAllDiscounts() {
        when(discountRepository.findAll()).thenReturn(Arrays.asList(discount));
        when(discountMapper.toDto(discount)).thenReturn(discountDto);

        List<DiscountDto> discounts = discountService.getAllDiscounts();

        assertNotNull(discounts);
        assertEquals(1, discounts.size());
        assertEquals(discountDto, discounts.get(0));
    }

    @Test
    void testGetDiscountById_Success() {
        when(discountRepository.findById(1L)).thenReturn(Optional.of(discount));
        when(discountMapper.toDto(discount)).thenReturn(discountDto);

        DiscountDto result = discountService.getDiscountById(1L);

        assertNotNull(result);
        assertEquals(discountDto, result);
    }

    @Test
    void testGetDiscountById_NotFound() {
        when(discountRepository.findById(1L)).thenReturn(Optional.empty());

        DiscountNotFoundException exception = assertThrows(DiscountNotFoundException.class, () -> discountService.getDiscountById(1L));
        assertEquals(ErrorMessage.DISCOUNT_NOT_FOUND, exception.getMessage());
    }

    // Дополнительные тесты можно добавить для методов create, update, delete и т.д.
}
