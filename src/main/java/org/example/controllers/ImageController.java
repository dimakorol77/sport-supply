package org.example.controllers;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.example.annotations.ImageAnnotations.DeleteImage;
import org.example.annotations.ImageAnnotations.GetImagesByProductId;
import org.example.annotations.ImageAnnotations.UploadImageFile;
import org.example.annotations.ImageAnnotations.UploadImageUrl;
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

public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @UploadImageFile
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageDto> uploadImageFile(
            @PathVariable Long productId,
            @Parameter(description = "File to upload", required = true, schema = @Schema(type = "string", format = "binary"))
            @RequestParam("file") MultipartFile file) {
        ImageDto imageDto = imageService.uploadImage(productId, file);
        return ResponseEntity.ok(imageDto);
    }

    @UploadImageUrl
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageDto> uploadImageUrl(
            @PathVariable Long productId,
            @RequestBody String imageUrl) {
        ImageDto imageDto = imageService.uploadImageByUrl(productId, imageUrl);
        return ResponseEntity.ok(imageDto);
    }

    @GetImagesByProductId
    // Доступен для всех пользователей
    public ResponseEntity<List<ImageDto>> getImagesByProductId(@PathVariable Long productId) {
        List<ImageDto> images = imageService.getImagesByProductId(productId);
        return ResponseEntity.ok(images);
    }

    @DeleteImage
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}

