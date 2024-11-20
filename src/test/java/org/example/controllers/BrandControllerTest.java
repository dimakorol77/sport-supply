// src/test/java/org/example/controllers/BrandControllerTest.java

package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.BrandDto;
import org.example.exceptions.BrandAlreadyExistsException;
import org.example.exceptions.BrandNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.services.interfaces.BrandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BrandControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BrandService brandService;

    @InjectMocks
    private BrandController brandController;

    private BrandDto brandDto;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(brandController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        brandDto = new BrandDto();
        brandDto.setId(1L);
        brandDto.setName("Test Brand");
        brandDto.setDescription("Test Description");
    }

    @Test
    void testGetAllBrands() throws Exception {
        when(brandService.getAllBrands()).thenReturn(Arrays.asList(brandDto));

        mockMvc.perform(get("/api/brands/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(brandDto.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(brandDto.getName())))
                .andExpect(jsonPath("$[0].description", is(brandDto.getDescription())));
    }

    @Test
    void testGetBrandById_Success() throws Exception {
        when(brandService.getBrandById(1L)).thenReturn(brandDto);

        mockMvc.perform(get("/api/brands/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(brandDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(brandDto.getName())))
                .andExpect(jsonPath("$.description", is(brandDto.getDescription())));
    }

    @Test
    void testGetBrandById_NotFound() throws Exception {
        when(brandService.getBrandById(1L)).thenThrow(new BrandNotFoundException(ErrorMessage.BRAND_NOT_FOUND));

        mockMvc.perform(get("/api/brands/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.BRAND_NOT_FOUND));
    }

    @Test
    void testCreateBrand_Success() throws Exception {
        when(brandService.createBrand(any(BrandDto.class))).thenReturn(brandDto);

        String brandJson = objectMapper.writeValueAsString(brandDto);

        mockMvc.perform(post("/api/brands/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brandJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(brandDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(brandDto.getName())))
                .andExpect(jsonPath("$.description", is(brandDto.getDescription())));
    }


    @Test
    void testCreateBrand_AlreadyExists() throws Exception {
        when(brandService.createBrand(any(BrandDto.class))).thenThrow(new BrandAlreadyExistsException(ErrorMessage.BRAND_ALREADY_EXISTS));

        String brandJson = objectMapper.writeValueAsString(brandDto);

        mockMvc.perform(post("/api/brands/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brandJson))
                .andExpect(status().isConflict())
                .andExpect(content().string(ErrorMessage.BRAND_ALREADY_EXISTS));
    }

    @Test
    void testUpdateBrand_Success() throws Exception {
        BrandDto updatedBrandDto = new BrandDto();
        updatedBrandDto.setId(1L);
        updatedBrandDto.setName("Updated Brand");
        updatedBrandDto.setDescription("Updated Description");

        when(brandService.updateBrand(eq(1L), any(BrandDto.class))).thenReturn(updatedBrandDto);

        String updatedBrandJson = objectMapper.writeValueAsString(updatedBrandDto);

        mockMvc.perform(put("/api/brands/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBrandJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedBrandDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(updatedBrandDto.getName())))
                .andExpect(jsonPath("$.description", is(updatedBrandDto.getDescription())));
    }

    @Test
    void testUpdateBrand_NotFound() throws Exception {
        BrandDto updatedBrandDto = new BrandDto();
        updatedBrandDto.setId(1L);
        updatedBrandDto.setName("Updated Brand");
        updatedBrandDto.setDescription("Updated Description");

        when(brandService.updateBrand(eq(1L), any(BrandDto.class))).thenThrow(new BrandNotFoundException(ErrorMessage.BRAND_NOT_FOUND));

        String updatedBrandJson = objectMapper.writeValueAsString(updatedBrandDto);

        mockMvc.perform(put("/api/brands/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBrandJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.BRAND_NOT_FOUND));
    }

    @Test
    void testDeleteBrand_Success() throws Exception {
        doNothing().when(brandService).deleteBrand(1L);

        mockMvc.perform(delete("/api/brands/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteBrand_NotFound() throws Exception {
        doThrow(new BrandNotFoundException(ErrorMessage.BRAND_NOT_FOUND)).when(brandService).deleteBrand(1L);

        mockMvc.perform(delete("/api/brands/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.BRAND_NOT_FOUND));
    }
}
