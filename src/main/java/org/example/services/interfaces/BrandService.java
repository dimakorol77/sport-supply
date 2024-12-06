package org.example.services.interfaces;

import org.example.dto.BrandDto;
import java.util.List;

public interface BrandService {
    List<BrandDto> getAllBrands();
    BrandDto getBrandById(Long id);
    BrandDto createBrand(BrandDto brandDto);
    BrandDto updateBrand(Long id, BrandDto brandDto);
    void deleteBrand(Long id);
}
