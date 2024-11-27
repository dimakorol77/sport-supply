package org.example.services.impl;

import org.example.dto.BrandDto;
import org.example.exceptions.BrandAlreadyExistsException;
import org.example.exceptions.BrandNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.BrandMapper;
import org.example.models.Brand;
import org.example.repositories.BrandRepository;
import org.example.services.interfaces.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
    public BrandDto getBrandById(Long id) {
        return brandRepository.findById(id)
                .map(brandMapper::toDto)
                .orElseThrow(() -> new BrandNotFoundException(ErrorMessage.BRAND_NOT_FOUND));
    }

    @Override
    public BrandDto createBrand(BrandDto brandDto) {
        brandRepository.findByName(brandDto.getName()).ifPresent(brand -> {
            throw new BrandAlreadyExistsException(ErrorMessage.BRAND_ALREADY_EXISTS);
        });

        Brand brand = brandMapper.toEntity(brandDto);
        brand.setCreatedAt(LocalDateTime.now());
        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.toDto(savedBrand);
    }

    @Override
    public BrandDto updateBrand(Long id, BrandDto brandDto) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException(ErrorMessage.BRAND_NOT_FOUND));

        brandMapper.updateEntityFromDto(brandDto, brand);
        brand.setUpdatedAt(LocalDateTime.now());
        Brand updatedBrand = brandRepository.save(brand);
        return brandMapper.toDto(updatedBrand);
    }

    @Override
    public void deleteBrand(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new BrandNotFoundException(ErrorMessage.BRAND_NOT_FOUND);
        }
        brandRepository.deleteById(id);
    }
}
