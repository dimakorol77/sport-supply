package org.example.services.impl;

import org.example.dto.ProductDto;
import org.example.enums.Role;
import org.example.exceptions.FavoriteAlreadyExistsException;
import org.example.exceptions.FavoriteNotFoundException;
import org.example.exceptions.ProductNotFoundException;

import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.ProductMapper;
import org.example.models.Favorite;
import org.example.models.Product;
import org.example.models.User;
import org.example.repositories.FavoriteRepository;
import org.example.repositories.ProductRepository;

import org.example.security.SecurityUtils;
import org.example.services.interfaces.FavoriteService;

import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final SecurityUtils securityUtils;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository,
                               ProductRepository productRepository,
                               ProductMapper productMapper,
                               SecurityUtils securityUtils) {
        this.favoriteRepository = favoriteRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.securityUtils = securityUtils;
    }

    private User getCurrentUser() {
        return securityUtils.getCurrentUser();
    }

    @Override
    public void addProductToFavorites(Long productId) {
        User currentUser = getCurrentUser();

        if (favoriteRepository.existsByUserIdAndProductId(currentUser.getId(), productId)) {
            throw new FavoriteAlreadyExistsException(ErrorMessage.FAVORITE_ALREADY_EXISTS);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));

        Favorite favorite = new Favorite();
        favorite.setUser(currentUser);
        favorite.setProduct(product);
        favorite.setAddedAt(LocalDateTime.now());

        favoriteRepository.save(favorite);
    }

    @Override
    public List<ProductDto> getUserFavorites() {
        User currentUser = getCurrentUser();

        List<Favorite> favorites = favoriteRepository.findByUserId(currentUser.getId());

        return favorites.stream()
                .map(favorite -> productMapper.toDto(favorite.getProduct()))
                .collect(Collectors.toList());
    }
    @Override
    public List<ProductDto> getUserFavoritesByAdmin(Long userId) {
        User currentUser = getCurrentUser();

        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }

        List<Favorite> favorites = favoriteRepository.findByUserId(userId);

        return favorites.stream()
                .map(favorite -> productMapper.toDto(favorite.getProduct()))
                .collect(Collectors.toList());
    }

    @Override
    public void removeProductFromFavorites(Long productId) {
        User currentUser = getCurrentUser();

        Favorite favorite = favoriteRepository.findByUserIdAndProductId(currentUser.getId(), productId)
                .orElseThrow(() -> new FavoriteNotFoundException(ErrorMessage.FAVORITE_NOT_FOUND));

        favoriteRepository.delete(favorite);
    }
}
