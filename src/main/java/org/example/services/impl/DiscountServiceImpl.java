package org.example.services.impl;

import org.example.dto.DiscountDto;
import org.example.exceptions.DiscountNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.DiscountMapper;
import org.example.models.Discount;
import org.example.repositories.DiscountRepository;
import org.example.services.interfaces.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final DiscountMapper discountMapper;

    @Autowired
    public DiscountServiceImpl(DiscountRepository discountRepository,
                               DiscountMapper discountMapper) {
        this.discountRepository = discountRepository;
        this.discountMapper = discountMapper;
    }

    @Override
    public List<DiscountDto> getAllDiscounts() {
        return discountRepository.findAll().stream()
                .map(discountMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DiscountDto getDiscountById(Long id) {
        return discountRepository.findById(id)
                .map(discountMapper::toDto)
                .orElseThrow(() -> new DiscountNotFoundException(ErrorMessage.DISCOUNT_NOT_FOUND));
    }

    @Override
    public DiscountDto createDiscount(DiscountDto discountDto) {
        Discount discount = discountMapper.toEntity(discountDto);
        discount.setCreatedAt(LocalDateTime.now());
        Discount savedDiscount = discountRepository.save(discount);
        return discountMapper.toDto(savedDiscount);
    }

    @Override
    public DiscountDto updateDiscount(Long id, DiscountDto discountDto) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new DiscountNotFoundException(ErrorMessage.DISCOUNT_NOT_FOUND));

        discountMapper.updateEntityFromDto(discountDto, discount);
        discount.setUpdatedAt(LocalDateTime.now());
        Discount updatedDiscount = discountRepository.save(discount);
        return discountMapper.toDto(updatedDiscount);
    }

    @Override
    public void deleteDiscount(Long id) {
        if (!discountRepository.existsById(id)) {
            throw new DiscountNotFoundException(ErrorMessage.DISCOUNT_NOT_FOUND);
        }
        discountRepository.deleteById(id);
    }

    @Override
    public List<DiscountDto> getActiveDiscountsForProduct(Long productId) {
        LocalDateTime now = LocalDateTime.now();
        List<Discount> discounts = discountRepository.findByProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(productId, now, now);
        return discounts.stream()
                .map(discountMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DiscountDto> getCurrentDiscountForProduct(Long productId) {
        LocalDateTime now = LocalDateTime.now();
        return discountRepository.findFirstByProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByDiscountPriceDesc(productId, now, now)
                .map(discountMapper::toDto);
    }
}
