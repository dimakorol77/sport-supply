package org.example.mappers;

import org.example.dto.ProductDto;
import org.example.enums.Form;
import org.example.enums.ProteinType;
import org.example.models.Brand;
import org.example.models.Category;
import org.example.models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        dto.setBrandId(product.getBrand() != null ? product.getBrand().getId() : null);
        dto.setProteinType(product.getProteinType() != null ? product.getProteinType().name() : null);
        dto.setVitaminGroup(product.getVitaminGroup());
        dto.setForm(product.getForm() != null ? product.getForm().name() : null);
        return dto;
    }

    public Product toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            product.setCategory(category);
        }
        if (dto.getBrandId() != null) {
            Brand brand = new Brand();
            brand.setId(dto.getBrandId());
            product.setBrand(brand);
        }
        if (dto.getProteinType() != null) {
            product.setProteinType(ProteinType.valueOf(dto.getProteinType()));
        }
        product.setVitaminGroup(dto.getVitaminGroup());
        if (dto.getForm() != null) {
            product.setForm(Form.valueOf(dto.getForm()));
        }
        return product;
    }

    public void updateEntityFromDto(ProductDto dto, Product product) {
        if (dto == null || product == null) {
            return;
        }
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }
        if (dto.getBrandId() != null) {
            Brand brand = new Brand();
            brand.setId(dto.getBrandId());
            product.setBrand(brand);
        } else {
            product.setBrand(null);
        }
        if (dto.getProteinType() != null) {
            product.setProteinType(ProteinType.valueOf(dto.getProteinType()));
        } else {
            product.setProteinType(null);
        }
        product.setVitaminGroup(dto.getVitaminGroup());
        if (dto.getForm() != null) {
            product.setForm(Form.valueOf(dto.getForm()));
        } else {
            product.setForm(null);
        }
    }
}
