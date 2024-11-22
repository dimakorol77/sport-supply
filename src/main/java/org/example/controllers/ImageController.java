package org.example.controllers;

import org.example.dto.ImageDto;
import org.example.services.interfaces.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/images")
@PreAuthorize("hasRole('ADMIN')")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // Загрузка изображения для продукта
    @PostMapping("/upload/{productId}")
    public ResponseEntity<ImageDto> uploadImage(@PathVariable Long productId, @RequestParam("file") MultipartFile file) {
        ImageDto imageDto = imageService.uploadImage(productId, file);
        return ResponseEntity.ok(imageDto);
    }

    // Получение изображений для продукта
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ImageDto>> getImagesByProductId(@PathVariable Long productId) {
        List<ImageDto> images = imageService.getImagesByProductId(productId);
        return ResponseEntity.ok(images);
    }

    // Удаление изображения
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}
