package org.example.services.interfaces;

import org.example.models.Discount;

import java.util.List;
import java.util.Optional;

public interface DiscountService {
    List<Discount> getAllDiscounts();
    Optional<Discount> getDiscountById(Long id);
    Discount createDiscount(Discount discount);
    Optional<Discount> updateDiscount(Long id, Discount updatedDiscount);
    void deleteDiscount(Long id);
    List<Discount> getActiveDiscountsForProduct(Long productId);
}
