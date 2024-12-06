package org.example.controllers;

import org.example.annotations.FavoriteAnnotations.AddProductToFavorites;
import org.example.annotations.FavoriteAnnotations.GetUserFavorites;
import org.example.annotations.FavoriteAnnotations.GetUserFavoritesByAdmin;
import org.example.annotations.FavoriteAnnotations.RemoveProductFromFavorites;
import org.example.dto.ProductDto;
import org.example.models.User;
import org.example.security.SecurityUtils;
import org.example.services.interfaces.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addProductToFavorites(@PathVariable Long productId) {
        favoriteService.addProductToFavorites(productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetUserFavorites
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProductDto>> getUserFavorites() {
        List<ProductDto> favorites = favoriteService.getUserFavorites();
        return ResponseEntity.ok(favorites);
    }
    @GetUserFavoritesByAdmin
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductDto>> getUserFavoritesByAdmin(@PathVariable Long userId) {
        List<ProductDto> favorites = favoriteService.getUserFavoritesByAdmin(userId);
        return ResponseEntity.ok(favorites);
    }

    @RemoveProductFromFavorites
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeProductFromFavorites(@PathVariable Long productId) {
        favoriteService.removeProductFromFavorites(productId);
        return ResponseEntity.noContent().build();
    }
}
