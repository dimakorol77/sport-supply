package org.example.services.impl;

import org.example.models.Discount;
import org.example.repositories.DiscountRepository;
import org.example.services.interfaces.DiscountService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    // Используем конструкторную инъекцию
    public DiscountServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Override
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    @Override
    public Optional<Discount> getDiscountById(Long id) {
        return discountRepository.findById(id);
    }

    @Override
    public Discount createDiscount(Discount discount) {
        discount.setCreatedAt(LocalDateTime.now());
        return discountRepository.save(discount);
    }

    @Override
    public Optional<Discount> updateDiscount(Long id, Discount updatedDiscount) {
        return discountRepository.findById(id).map(discount -> {
            discount.setDiscountPrice(updatedDiscount.getDiscountPrice());
            discount.setStartDate(updatedDiscount.getStartDate());
            discount.setEndDate(updatedDiscount.getEndDate());
            discount.setUpdatedAt(LocalDateTime.now());
            return discountRepository.save(discount);
        });
    }

    @Override
    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }

    @Override
    public List<Discount> getActiveDiscountsForProduct(Long productId) {
        LocalDateTime now = LocalDateTime.now();
        return discountRepository.findByProductIdAndStartDateBeforeAndEndDateAfter(productId, now, now);
    }
}
