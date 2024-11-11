package org.example.services.impl;

import org.example.dto.DiscountDto;
import org.example.dto.ProductDto;
import org.example.dto.PromotionDto;
import org.example.mappers.DiscountMapper;
import org.example.mappers.ProductMapper;
import org.example.mappers.PromotionMapper;
import org.example.models.Product;
import org.example.models.ProductPromotion;
import org.example.models.Promotion;
import org.example.repositories.DiscountRepository;
import org.example.repositories.ProductPromotionRepository;
import org.example.repositories.ProductRepository;
import org.example.services.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final ProductPromotionRepository productPromotionRepository;
    private final ProductMapper productMapper;
    private final DiscountMapper discountMapper;
    private final PromotionMapper promotionMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              DiscountRepository discountRepository,
                              ProductPromotionRepository productPromotionRepository,
                              ProductMapper productMapper,
                              DiscountMapper discountMapper,
                              PromotionMapper promotionMapper) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.productPromotionRepository = productPromotionRepository;
        this.productMapper = productMapper;
        this.discountMapper = discountMapper;
        this.promotionMapper = promotionMapper;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDto> getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        product.setCreatedAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Override
    public Optional<ProductDto> updateProduct(Long id, ProductDto productDto) {
        return productRepository.findById(id).map(product -> {
            productMapper.updateEntityFromDto(productDto, product);
            product.setUpdatedAt(LocalDateTime.now());
            Product updatedProduct = productRepository.save(product);
            return productMapper.toDto(updatedProduct);
        });
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<DiscountDto> getActiveDiscounts(Long productId) {
        LocalDateTime now = LocalDateTime.now();
        return discountRepository.findByProductIdAndStartDateBeforeAndEndDateAfter(productId, now, now).stream()
                .map(discountMapper::toDto)
                .collect(Collectors.toList());
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
