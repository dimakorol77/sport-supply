package org.example.services.impl;

import org.example.dto.BrandDto;
import org.example.mappers.BrandMapper;
import org.example.models.Brand;
import org.example.repositories.BrandRepository;
import org.example.services.interfaces.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository, BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    @Override
    public List<BrandDto> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BrandDto> getBrandById(Long id) {
        return brandRepository.findById(id)
                .map(brandMapper::toDto);
    }

    @Override
    public BrandDto createBrand(BrandDto brandDto) {
        Brand brand = brandMapper.toEntity(brandDto);
        brand.setCreatedAt(LocalDateTime.now());
        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.toDto(savedBrand);
    }

    @Override
    public Optional<BrandDto> updateBrand(Long id, BrandDto brandDto) {
        return brandRepository.findById(id).map(brand -> {
            brandMapper.updateEntityFromDto(brandDto, brand);
            brand.setUpdatedAt(LocalDateTime.now());
            Brand updatedBrand = brandRepository.save(brand);
            return brandMapper.toDto(updatedBrand);
        });
    }

    @Override
    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }
}
