package org.example.specifications;

import org.example.models.Product;
import org.example.models.Discount;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductSpecification {

    // Фильтрация по категории
    public static Specification<Product> hasCategory(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    // Фильтрация по цене (мин и макс)
    public static Specification<Product> hasPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
    }

    // Фильтрация по наличию активной скидки
    public static Specification<Product> hasActiveDiscount() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime now = LocalDateTime.now();
            Join<Product, Discount> discountJoin = root.join("discounts");
            return criteriaBuilder.and(
                    criteriaBuilder.lessThanOrEqualTo(discountJoin.get("startDate"), now),
                    criteriaBuilder.greaterThanOrEqualTo(discountJoin.get("endDate"), now)
            );
        };
    }

    // Сортировка по указанному полю и направлению
    public static Specification<Product> sortBy(String field, boolean asc) {
        return (root, query, criteriaBuilder) -> {
            if (asc) {
                query.orderBy(criteriaBuilder.asc(root.get(field)));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get(field)));
            }
            return criteriaBuilder.conjunction();
        };
    }
}
