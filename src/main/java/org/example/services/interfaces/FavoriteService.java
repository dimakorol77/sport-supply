package org.example.services.interfaces;

import org.example.dto.ProductDto;

import java.util.List;

public interface FavoriteService {
    void addProductToFavorites(Long userId, Long productId);

    List<ProductDto> getUserFavorites(Long userId);

    void removeProductFromFavorites(Long userId, Long productId);
}
