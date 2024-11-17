package org.example.services.interfaces;

import org.example.dto.DiscountDto;
import java.util.List;
import java.util.Optional;

public interface DiscountService {
    List<DiscountDto> getAllDiscounts();
    DiscountDto getDiscountById(Long id);
    DiscountDto createDiscount(DiscountDto discountDto);
    DiscountDto updateDiscount(Long id, DiscountDto discountDto);
    void deleteDiscount(Long id);
    List<DiscountDto> getActiveDiscountsForProduct(Long productId);
    Optional<DiscountDto> getCurrentDiscountForProduct(Long productId);

}
