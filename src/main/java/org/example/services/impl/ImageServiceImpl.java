package org.example.services.impl;

import org.example.dto.ImageDto;
import org.example.exception.ImageNotFoundException;
import org.example.exception.ImageUploadException;
import org.example.exception.ProductNotFoundException;
import org.example.exception.errorMessage.ErrorMessage;
import org.example.mappers.ImageMapper;
import org.example.models.Image;
import org.example.models.Product;
import org.example.repositories.ImageRepository;
import org.example.repositories.ProductRepository;
import org.example.services.interfaces.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final ImageMapper imageMapper;

    // Директория для хранения изображений
    private final String uploadDir = "uploads/images/";

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository,
                            ProductRepository productRepository,
                            ImageMapper imageMapper) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
        this.imageMapper = imageMapper;

        // Создаем директорию, если ее нет
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new ImageUploadException(ErrorMessage.IMAGE_UPLOAD_FAILED);
        }
    }

    @Override
    public ImageDto uploadImage(Long productId, MultipartFile file) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));

        String fileName = saveFile(file);

        Image image = new Image();
        image.setUrl("/" + uploadDir + fileName);
        image.setAltText(file.getOriginalFilename());
        image.setProduct(product);
        image.setCreatedAt(LocalDateTime.now());
        image.setUpdatedAt(LocalDateTime.now());

        Image savedImage = imageRepository.save(image);
        return imageMapper.toDto(savedImage);
    }

    @Override
    public List<ImageDto> getImagesByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND);
        }
        List<Image> images = imageRepository.findByProductId(productId);
        return images.stream().map(imageMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException(ErrorMessage.IMAGE_NOT_FOUND));
        deleteFile(image.getUrl());
        imageRepository.deleteById(imageId);
    }

    // Метод для сохранения файла на диск
    private String saveFile(MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        try {
            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new ImageUploadException(ErrorMessage.IMAGE_UPLOAD_FAILED);
        }
        return fileName;
    }

    // Метод для удаления файла с диска
    private void deleteFile(String fileUrl) {
        try {
            Path filePath = Paths.get(fileUrl.substring(1)); // Убираем первый слеш
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new ImageNotFoundException(ErrorMessage.IMAGE_NOT_FOUND);
        }
    }
}
