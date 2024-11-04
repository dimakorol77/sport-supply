package org.example.services.impl;

import org.example.models.Brand;
import org.example.repositories.BrandRepository;
import org.example.services.interfaces.BrandService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    // Используем конструкторную инъекцию
    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    // Получить все бренды
    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    // Получить бренд по ID
    @Override
    public Optional<Brand> getBrandById(Long id) {
        return brandRepository.findById(id);
    }

    // Создать новый бренд
    @Override
    public Brand createBrand(Brand brand) {
        brand.setCreatedAt(LocalDateTime.now());
        return brandRepository.save(brand);
    }

    // Обновить бренд
    @Override
    public Optional<Brand> updateBrand(Long id, Brand updatedBrand) {
        return brandRepository.findById(id).map(brand -> {
            brand.setName(updatedBrand.getName());
            brand.setDescription(updatedBrand.getDescription());
            brand.setUpdatedAt(LocalDateTime.now());
            // Обновление других полей при необходимости
            return brandRepository.save(brand);
        });
    }

    // Удалить бренд
    @Override
    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }
}
