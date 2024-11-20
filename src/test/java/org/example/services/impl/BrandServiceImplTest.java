// src/test/java/org/example/services/impl/BrandServiceImplTest.java

package org.example.services.impl;

import org.example.dto.BrandDto;
import org.example.exceptions.BrandAlreadyExistsException;
import org.example.exceptions.BrandNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.BrandMapper;
import org.example.models.Brand;
import org.example.repositories.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BrandServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandServiceImpl brandService;

    private Brand brand;
    private BrandDto brandDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        brand = new Brand();
        brand.setId(1L);
        brand.setName("Test Brand");
        brand.setDescription("Test Description");
        brand.setCreatedAt(LocalDateTime.now());

        brandDto = new BrandDto();
        brandDto.setId(1L);
        brandDto.setName("Test Brand");
        brandDto.setDescription("Test Description");
    }

    @Test
    void testGetAllBrands() {
        when(brandRepository.findAll()).thenReturn(Arrays.asList(brand));
        when(brandMapper.toDto(brand)).thenReturn(brandDto);

        List<BrandDto> brands = brandService.getAllBrands();

        assertNotNull(brands);
        assertEquals(1, brands.size());
        assertEquals(brandDto, brands.get(0));
    }

    @Test
    void testGetBrandById_Success() {
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(brandMapper.toDto(brand)).thenReturn(brandDto);

        BrandDto result = brandService.getBrandById(1L);

        assertNotNull(result);
        assertEquals(brandDto, result);
    }

    @Test
    void testGetBrandById_NotFound() {
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        BrandNotFoundException exception = assertThrows(BrandNotFoundException.class, () -> brandService.getBrandById(1L));
        assertEquals(ErrorMessage.BRAND_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testCreateBrand_Success() {
        when(brandRepository.findByName("Test Brand")).thenReturn(Optional.empty());
        when(brandMapper.toEntity(brandDto)).thenReturn(brand);
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);
        when(brandMapper.toDto(brand)).thenReturn(brandDto);

        BrandDto result = brandService.createBrand(brandDto);

        assertNotNull(result);
        assertEquals(brandDto, result);
        verify(brandRepository, times(1)).save(brand);
    }

    @Test
    void testCreateBrand_AlreadyExists() {
        when(brandRepository.findByName("Test Brand")).thenReturn(Optional.of(brand));

        BrandAlreadyExistsException exception = assertThrows(BrandAlreadyExistsException.class, () -> brandService.createBrand(brandDto));
        assertEquals(ErrorMessage.BRAND_ALREADY_EXISTS, exception.getMessage());
        verify(brandRepository, never()).save(any());
    }

    @Test
    void testUpdateBrand_Success() {
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        doNothing().when(brandMapper).updateEntityFromDto(brandDto, brand);
        when(brandRepository.save(brand)).thenReturn(brand);
        when(brandMapper.toDto(brand)).thenReturn(brandDto);

        BrandDto result = brandService.updateBrand(1L, brandDto);

        assertNotNull(result);
        assertEquals(brandDto, result);
    }

    @Test
    void testUpdateBrand_NotFound() {
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        BrandNotFoundException exception = assertThrows(BrandNotFoundException.class, () -> brandService.updateBrand(1L, brandDto));
        assertEquals(ErrorMessage.BRAND_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testDeleteBrand_Success() {
        when(brandRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> brandService.deleteBrand(1L));
        verify(brandRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBrand_NotFound() {
        when(brandRepository.existsById(1L)).thenReturn(false);

        BrandNotFoundException exception = assertThrows(BrandNotFoundException.class, () -> brandService.deleteBrand(1L));
        assertEquals(ErrorMessage.BRAND_NOT_FOUND, exception.getMessage());
        verify(brandRepository, never()).deleteById(anyLong());
    }
}
