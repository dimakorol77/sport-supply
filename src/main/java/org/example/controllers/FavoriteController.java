package org.example.controllers;

import org.example.annotations.FavoriteAnnotations.AddProductToFavorites;
import org.example.annotations.FavoriteAnnotations.GetUserFavorites;
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
    private final SecurityUtils securityUtils;


    @Autowired
    public FavoriteController(FavoriteService favoriteService, SecurityUtils securityUtils) {
        this.favoriteService = favoriteService;
        this.securityUtils = securityUtils;
    }

    private User getCurrentUser() {
        return securityUtils.getCurrentUser();
    }
    @AddProductToFavorites
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addProductToFavorites(@PathVariable Long productId) {
        User user = getCurrentUser();
        favoriteService.addProductToFavorites(user.getId(), productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetUserFavorites
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProductDto>> getUserFavorites() {
        User user = getCurrentUser();
        List<ProductDto> favorites = favoriteService.getUserFavorites(user.getId());
        return ResponseEntity.ok(favorites);
    }

    @RemoveProductFromFavorites
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeProductFromFavorites(@PathVariable Long productId) {
        User user = getCurrentUser();
        favoriteService.removeProductFromFavorites(user.getId(), productId);
        return ResponseEntity.noContent().build();
    }
}
