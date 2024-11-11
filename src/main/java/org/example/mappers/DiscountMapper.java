package org.example.mappers;

import org.example.dto.DiscountDto;
import org.example.models.Discount;
import org.example.models.Product;
import org.springframework.stereotype.Component;

@Component
public class DiscountMapper {

    public DiscountDto toDto(Discount discount) {
        if (discount == null) {
            return null;
        }
        DiscountDto dto = new DiscountDto();
        dto.setId(discount.getId());
        dto.setProductId(discount.getProduct() != null ? discount.getProduct().getId() : null);
        dto.setDiscountPrice(discount.getDiscountPrice());
        dto.setStartDate(discount.getStartDate());
        dto.setEndDate(discount.getEndDate());
        return dto;
    }

    public Discount toEntity(DiscountDto dto) {
        if (dto == null) {
            return null;
        }
        Discount discount = new Discount();
        discount.setDiscountPrice(dto.getDiscountPrice());
        discount.setStartDate(dto.getStartDate());
        discount.setEndDate(dto.getEndDate());
        if (dto.getProductId() != null) {
            Product product = new Product();
            product.setId(dto.getProductId());
            discount.setProduct(product);
        }
        return discount;
    }

    public void updateEntityFromDto(DiscountDto dto, Discount discount) {
        if (dto == null || discount == null) {
            return;
        }
        discount.setDiscountPrice(dto.getDiscountPrice());
        discount.setStartDate(dto.getStartDate());
        discount.setEndDate(dto.getEndDate());
        if (dto.getProductId() != null) {
            Product product = new Product();
            product.setId(dto.getProductId());
            discount.setProduct(product);
        } else {
            discount.setProduct(null);
        }
    }
}
