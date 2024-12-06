package org.example.services.interfaces;

import org.example.dto.ProductDto;

import java.util.List;

public interface FavoriteService {
    void addProductToFavorites(Long productId);
    List<ProductDto> getUserFavorites();
    List<ProductDto> getUserFavoritesByAdmin(Long userId);
    void removeProductFromFavorites(Long productId);
}
