package org.example.controllers;

import org.example.annotation.favorite.AddProductToFavorites;
import org.example.annotation.favorite.GetUserFavorites;
import org.example.annotation.favorite.RemoveProductFromFavorites;
import org.example.dto.ProductDto;
import org.example.services.impl.FavoriteServiceImpl;
import org.example.services.interfaces.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @AddProductToFavorites
    public ResponseEntity<Void> addProductToFavorites(@PathVariable Long userId, @PathVariable Long productId) {
        favoriteService.addProductToFavorites(userId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetUserFavorites
    public ResponseEntity<List<ProductDto>> getUserFavorites(@PathVariable Long userId) {
        List<ProductDto> favorites = favoriteService.getUserFavorites(userId);
        return ResponseEntity.ok(favorites);
    }

    @RemoveProductFromFavorites
    public ResponseEntity<Void> removeProductFromFavorites(@PathVariable Long userId, @PathVariable Long productId) {
        favoriteService.removeProductFromFavorites(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
