package org.example.services.impl;

import org.example.dto.DiscountDto;
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
    public Optional<DiscountDto> getDiscountById(Long id) {
        return discountRepository.findById(id)
                .map(discountMapper::toDto);
    }

    @Override
    public DiscountDto createDiscount(DiscountDto discountDto) {
        Discount discount = discountMapper.toEntity(discountDto);
        discount.setCreatedAt(LocalDateTime.now());
        Discount savedDiscount = discountRepository.save(discount);
        return discountMapper.toDto(savedDiscount);
    }

    @Override
    public Optional<DiscountDto> updateDiscount(Long id, DiscountDto discountDto) {
        return discountRepository.findById(id).map(discount -> {
            discountMapper.updateEntityFromDto(discountDto, discount);
            discount.setUpdatedAt(LocalDateTime.now());
            Discount updatedDiscount = discountRepository.save(discount);
            return discountMapper.toDto(updatedDiscount);
        });
    }

    @Override
    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }

    @Override
    public List<DiscountDto> getActiveDiscountsForProduct(Long productId) {
        LocalDateTime now = LocalDateTime.now();
        return discountRepository.findByProductIdAndStartDateBeforeAndEndDateAfter(productId, now, now).stream()
                .map(discountMapper::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public Optional<DiscountDto> getCurrentDiscountForProduct(Long productId) {
        LocalDateTime now = LocalDateTime.now();
        return discountRepository.findFirstByProductIdAndStartDateBeforeAndEndDateAfterOrderByDiscountPriceDesc(productId, now, now)
                .map(discountMapper::toDto);
    }
}
