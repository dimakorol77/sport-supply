package org.example.services.impl;

import org.example.dto.PromotionDto;
import org.example.exceptions.ProductAlreadyInPromotionException;
import org.example.exceptions.ProductNotFoundException;
import org.example.exceptions.PromotionNotFoundException;
import org.example.exceptions.ProductPromotionNotFoundException;
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
        Promotion promotion = promotionMapper.toEntity(promotionDto);
        Promotion savedPromotion = promotionRepository.save(promotion);
        return promotionMapper.toDto(savedPromotion);
    }

    @Override
    public PromotionDto updatePromotion(Long id, PromotionDto promotionDto) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new PromotionNotFoundException(ErrorMessage.PROMOTION_NOT_FOUND));

        promotionMapper.updateEntityFromDto(promotionDto, promotion);
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

        // Проверяем, существует ли уже связь
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
        List<ProductPromotion> productPromotions = productPromotionRepository.findByProductId(productId);
        return productPromotions.stream()
                .map(ProductPromotion::getPromotion)
                .map(promotionMapper::toDto)
                .collect(Collectors.toList());
    }

    
}
