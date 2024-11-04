package org.example.services.interfaces;

import org.example.models.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<Brand> getAllBrands();
    Optional<Brand> getBrandById(Long id);
    Brand createBrand(Brand brand);
    Optional<Brand> updateBrand(Long id, Brand updatedBrand);
    void deleteBrand(Long id);
}
