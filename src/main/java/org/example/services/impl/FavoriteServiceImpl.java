package org.example.services.impl;

import org.example.dto.ProductDto;
import org.example.exceptions.FavoriteAlreadyExistsException;
import org.example.exceptions.FavoriteNotFoundException;
import org.example.exceptions.ProductNotFoundException;
import org.example.exceptions.UserNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.ProductMapper;
import org.example.models.Favorite;
import org.example.models.Product;
import org.example.models.User;
import org.example.repositories.FavoriteRepository;
import org.example.repositories.ProductRepository;
import org.example.repositories.UserRepository;
import org.example.services.interfaces.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public FavoriteServiceImpl(FavoriteRepository favoriteRepository,
                               UserRepository userRepository,
                               ProductRepository productRepository,
                               ProductMapper productMapper) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public void addProductToFavorites(Long userId, Long productId) {
        if (favoriteRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new FavoriteAlreadyExistsException(ErrorMessage.FAVORITE_ALREADY_EXISTS);
        }

        // Получаем пользователя по userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessage.USER_NOT_FOUND));

        // Получаем продукт по productId
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));

        // Создаем новый объект Favorite и устанавливаем пользователя и продукт
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        favorite.setAddedAt(LocalDateTime.now());

        favoriteRepository.save(favorite);
    }

    @Override
    public List<ProductDto> getUserFavorites(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);

        return favorites.stream()
                .map(favorite -> productMapper.toDto(favorite.getProduct()))
                .collect(Collectors.toList());
    }

    @Override
    public void removeProductFromFavorites(Long userId, Long productId) {
        Favorite favorite = favoriteRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new FavoriteNotFoundException(ErrorMessage.FAVORITE_NOT_FOUND));

        // Проверка прав доступа
        if (!favorite.getUser().getId().equals(userId)) {
            throw new org.springframework.security.access.AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }

        favoriteRepository.delete(favorite);
    }
}
