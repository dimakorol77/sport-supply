package org.example.services.impl;

import org.example.dto.DiscountDto;
import org.example.dto.ProductDto;
import org.example.dto.PromotionDto;
import org.example.exceptions.ProductAlreadyExistsException;
import org.example.exceptions.ProductNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.DiscountMapper;
import org.example.mappers.ProductMapper;
import org.example.mappers.PromotionMapper;
import org.example.models.Discount;
import org.example.models.Product;
import org.example.models.ProductPromotion;
import org.example.repositories.DiscountRepository;
import org.example.repositories.ProductPromotionRepository;
import org.example.repositories.ProductRepository;
import org.example.services.interfaces.ProductService;
import org.example.specifications.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final ProductMapper productMapper;
    private final DiscountMapper discountMapper;


    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              DiscountRepository discountRepository,
                              ProductMapper productMapper,
                              DiscountMapper discountMapper
                              ) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.productMapper = productMapper;
        this.discountMapper = discountMapper;

    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));
    }

    @Override
    public ProductDto createProduct(@Validated(ProductDto.OnCreate.class) ProductDto productDto) {

        productRepository.findByName(productDto.getName()).ifPresent(product -> {
            throw new ProductAlreadyExistsException(ErrorMessage.PRODUCT_ALREADY_EXISTS);
        });

        Product product = productMapper.toEntity(productDto);
        product.setCreatedAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));

        productMapper.updateEntityFromDto(productDto, product);
        product.setUpdatedAt(LocalDateTime.now());
        Product updatedProduct = productRepository.save(product);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND);
        }
        productRepository.deleteById(id);
    }



    @Override
    public List<ProductDto> filterAndSortProducts(BigDecimal minPrice, BigDecimal maxPrice, Long categoryId, Boolean hasDiscount, String sortBy, Boolean asc) {
        Specification<Product> spec = Specification.where(null);

        if (categoryId != null) {
            spec = spec.and(ProductSpecification.hasCategory(categoryId));
        }

        if (minPrice != null && maxPrice != null) {
            spec = spec.and(ProductSpecification.hasPriceBetween(minPrice, maxPrice));
        } else if (minPrice != null) {
            spec = spec.and(ProductSpecification.hasPriceBetween(minPrice, BigDecimal.valueOf(Double.MAX_VALUE)));
        } else if (maxPrice != null) {
            spec = spec.and(ProductSpecification.hasPriceBetween(BigDecimal.ZERO, maxPrice));
        }

        if (Boolean.TRUE.equals(hasDiscount)) {
            spec = spec.and(ProductSpecification.hasActiveDiscount());
        }

        if (!isSortableField(sortBy)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_SORT_FIELD + sortBy);
        }

        // Создаём объект PageRequest с сортировкой
        Sort sort = Sort.by(Boolean.TRUE.equals(asc) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort); // Используем Pageable для сортировки

        // Вызов метода репозитория
        List<Product> products = productRepository.findAll(spec, pageable).getContent();
        return products.stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    private boolean isSortableField(String field) {
        return field.equals("name") || field.equals("price") || field.equals("createdAt");
    }


}
