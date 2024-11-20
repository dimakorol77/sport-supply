// src/test/java/org/example/services/impl/ImageServiceImplTest.java

package org.example.services.impl;

import org.example.dto.ImageDto;
import org.example.exceptions.ImageNotFoundException;
import org.example.exceptions.ImageUploadException;
import org.example.exceptions.ProductNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.ImageMapper;
import org.example.models.Image;
import org.example.models.Product;
import org.example.repositories.ImageRepository;
import org.example.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ImageMapper imageMapper;

    @InjectMocks
    private ImageServiceImpl imageService;

    private Product product;
    private Image image;
    private ImageDto imageDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");

        image = new Image();
        image.setId(1L);
        image.setUrl("/uploads/images/test.jpg");
        image.setAltText("test.jpg");
        image.setProduct(product);
        image.setCreatedAt(LocalDateTime.now());
        image.setUpdatedAt(LocalDateTime.now());

        imageDto = new ImageDto();
        imageDto.setId(1L);
        imageDto.setUrl("/uploads/images/test.jpg");
        imageDto.setAltText("test.jpg");
    }

    @Test
    void testUploadImage_Success() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "dummy content".getBytes());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(imageMapper.toDto(any(Image.class))).thenReturn(imageDto);
        when(imageRepository.save(any(Image.class))).thenReturn(image);

        ImageDto result = imageService.uploadImage(1L, file);

        assertNotNull(result);
        assertEquals(imageDto, result);
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    void testUploadImage_ProductNotFound() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "dummy content".getBytes());

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> imageService.uploadImage(1L, file));
        assertEquals(ErrorMessage.PRODUCT_NOT_FOUND, exception.getMessage());
        verify(imageRepository, never()).save(any());
    }

    @Test
    void testUploadImage_ImageUploadFailed() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "dummy content".getBytes());

        // Симулируем исключение при сохранении файла
        doThrow(new IOException("Failed to save file")).when(imageService).saveFile(file);

        ImageUploadException exception = assertThrows(ImageUploadException.class, () -> imageService.uploadImage(1L, file));
        assertEquals(ErrorMessage.IMAGE_UPLOAD_FAILED, exception.getMessage());
        verify(imageRepository, never()).save(any());
    }

    @Test
    void testGetImagesByProductId_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(imageRepository.findByProductId(1L)).thenReturn(Arrays.asList(image));
        when(imageMapper.toDto(image)).thenReturn(imageDto);

        List<ImageDto> images = imageService.getImagesByProductId(1L);

        assertNotNull(images);
        assertEquals(1, images.size());
        assertEquals(imageDto, images.get(0));
    }

    @Test
    void testGetImagesByProductId_ProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> imageService.getImagesByProductId(1L));
        assertEquals(ErrorMessage.PRODUCT_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testDeleteImage_Success() {
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

        assertDoesNotThrow(() -> imageService.deleteImage(1L));
        verify(imageRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteImage_NotFound() {
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        ImageNotFoundException exception = assertThrows(ImageNotFoundException.class, () -> imageService.deleteImage(1L));
        assertEquals(ErrorMessage.IMAGE_NOT_FOUND, exception.getMessage());
        verify(imageRepository, never()).deleteById(anyLong());
    }
}
