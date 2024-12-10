package org.example.specifications;

import org.example.models.Product;
import org.example.models.Discount;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductSpecification {


    public static Specification<Product> hasCategory(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }


    public static Specification<Product> hasPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
    }


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


}
