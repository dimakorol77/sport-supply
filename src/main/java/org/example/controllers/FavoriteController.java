package org.example.controllers;

import org.example.annotations.FavoriteAnnotations.AddProductToFavorites;
import org.example.annotations.FavoriteAnnotations.GetUserFavorites;
import org.example.annotations.FavoriteAnnotations.RemoveProductFromFavorites;
import org.example.dto.ProductDto;
import org.example.models.User;
import org.example.security.SecurityUtils;
import org.example.services.interfaces.FavoriteService;
import org.example.services.interfaces.UserService;
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
    private final UserService userService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService, UserService userService) {
        this.favoriteService = favoriteService;
        this.userService = userService;
    }

    @AddProductToFavorites
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addProductToFavorites(@PathVariable Long productId) {
        String email = SecurityUtils.getCurrentUserEmail();
        User user = userService.getUserByEmail(email);

        favoriteService.addProductToFavorites(user.getId(), productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetUserFavorites
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProductDto>> getUserFavorites() {
        String email = SecurityUtils.getCurrentUserEmail();
        User user = userService.getUserByEmail(email);

        List<ProductDto> favorites = favoriteService.getUserFavorites(user.getId());
        return ResponseEntity.ok(favorites);
    }

    @RemoveProductFromFavorites
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeProductFromFavorites(@PathVariable Long productId) {
        String email = SecurityUtils.getCurrentUserEmail();
        User user = userService.getUserByEmail(email);

        favoriteService.removeProductFromFavorites(user.getId(), productId);
        return ResponseEntity.noContent().build();
    }
}
