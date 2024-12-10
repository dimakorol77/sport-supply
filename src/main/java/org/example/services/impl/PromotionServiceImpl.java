package org.example.services.impl;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.example.dto.PromotionDto;
import org.example.exceptions.*;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.PromotionMapper;
import org.example.models.Product;
import org.example.models.ProductPromotion;
import org.example.models.Promotion;
import org.example.repositories.ProductPromotionRepository;
import org.example.repositories.ProductRepository;
import org.example.repositories.PromotionRepository;
import org.example.services.interfaces.PromotionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromotionServiceImpl implements PromotionService {


    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;
    private final ProductPromotionRepository productPromotionRepository;
    private final PromotionMapper promotionMapper;

    @Autowired
    public PromotionServiceImpl(PromotionRepository promotionRepository,
                                ProductRepository productRepository,
                                ProductPromotionRepository productPromotionRepository,
                                PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
        this.productPromotionRepository = productPromotionRepository;
        this.promotionMapper = promotionMapper;
    }

    @Override
    public List<PromotionDto> getAllPromotions() {
        List<Promotion> promotions = promotionRepository.findAll();
        return promotions.stream()
                .map(promotionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PromotionDto getPromotionById(Long id) {
        return promotionRepository.findById(id)
                .map(promotionMapper::toDto)
                .orElseThrow(() -> new PromotionNotFoundException(ErrorMessage.PROMOTION_NOT_FOUND));
    }

    @Override
    public PromotionDto createPromotion(PromotionDto promotionDto) {
        if (promotionRepository.existsByName(promotionDto.getName())) {
            throw new PromotionDuplicateEntityException(ErrorMessage.PROMOTION_DUPLICATE_ENTITY_EXCEPTION);
        }
        Promotion promotion = promotionMapper.toEntity(promotionDto);
        Promotion savedPromotion = promotionRepository.save(promotion);
        return promotionMapper.toDto(savedPromotion);
    }


    @Override
    public PromotionDto updatePromotion(Long id, PromotionDto promotionDto) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new PromotionNotFoundException(ErrorMessage.PROMOTION_NOT_FOUND));

        promotion.setName(promotionDto.getName());
        promotion.setDescription(promotionDto.getDescription());
        promotion.setStartDate(promotionDto.getStartDate());
        promotion.setEndDate(promotionDto.getEndDate());

        Promotion updatedPromotion = promotionRepository.save(promotion);
        return promotionMapper.toDto(updatedPromotion);
    }


    @Override
    public void deletePromotion(Long id) {
        if (!promotionRepository.existsById(id)) {
            throw new PromotionNotFoundException(ErrorMessage.PROMOTION_NOT_FOUND);
        }
        promotionRepository.deleteById(id);
    }

    @Override
    public void addProductToPromotion(Long promotionId, Long productId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new PromotionNotFoundException(ErrorMessage.PROMOTION_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));

        productPromotionRepository.findByProductIdAndPromotionId(productId, promotionId).ifPresent(pp -> {
            throw new ProductAlreadyInPromotionException(ErrorMessage.PRODUCT_ALREADY_IN_PROMOTION);
        });
        ProductPromotion productPromotion = new ProductPromotion();
        productPromotion.setProduct(product);
        productPromotion.setPromotion(promotion);
        productPromotionRepository.save(productPromotion);
    }

    @Override
    public void removeProductFromPromotion(Long promotionId, Long productId) {
        ProductPromotion productPromotion = productPromotionRepository.findByProductIdAndPromotionId(productId, promotionId)
                .orElseThrow(() -> new ProductPromotionNotFoundException(ErrorMessage.PRODUCT_PROMOTION_NOT_FOUND));

        productPromotionRepository.delete(productPromotion);
    }

    @Override
    public List<PromotionDto> getPromotionsForProduct(Long productId) {
        LocalDateTime now = LocalDateTime.now();
        List<ProductPromotion> productPromotions = productPromotionRepository.findByProductId(productId);

        return productPromotions.stream()
                .map(ProductPromotion::getPromotion)
                .filter(promotion -> promotion.getStartDate() != null && promotion.getEndDate() != null &&
                        !promotion.getStartDate().isAfter(now) &&
                        !promotion.getEndDate().isBefore(now))
                .map(promotionMapper::toDto)
                .collect(Collectors.toList());
    }



    @Override
    public BigDecimal getPromotionDiscountForProduct(Long productId) {
        LocalDateTime now = LocalDateTime.now();
        List<ProductPromotion> productPromotions = productPromotionRepository.findByProductId(productId);

        return productPromotions.stream()
                .map(ProductPromotion::getPromotion)
                .filter(promotion -> !promotion.getStartDate().isAfter(now) && !promotion.getEndDate().isBefore(now))
                .map(promotion -> extractDiscountFromPromotion(promotion.getDescription()))
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal extractDiscountFromPromotion(String description) {
        if (description == null || description.isEmpty()) return BigDecimal.ZERO;

        Pattern percentPattern = Pattern.compile("(\\d+)%");
        Matcher matcher = percentPattern.matcher(description);

        if (matcher.find()) {
            String percentage = matcher.group(1);
            return new BigDecimal(percentage).divide(BigDecimal.valueOf(100));
        }

        Pattern fixedPattern = Pattern.compile("(\\d+)\\s*eur");
        matcher = fixedPattern.matcher(description);

        if (matcher.find()) {
            return new BigDecimal(matcher.group(1));
        }

        return BigDecimal.ZERO;
    }


}
