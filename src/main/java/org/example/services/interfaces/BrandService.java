package org.example.services.interfaces;

import org.example.dto.BrandDto;
import org.example.models.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<BrandDto> getAllBrands();
    Optional<BrandDto> getBrandById(Long id);
    BrandDto createBrand(BrandDto brandDto);
    Optional<BrandDto> updateBrand(Long id, BrandDto brandDto);
    void deleteBrand(Long id);
}

