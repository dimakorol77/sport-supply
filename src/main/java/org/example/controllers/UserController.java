package org.example.controllers;

import jakarta.validation.Valid;


import org.example.annotations.UserAnnotations.*;
import org.example.dto.*;
import org.example.enums.Role;
import org.example.models.User;
import org.example.security.SecurityUtils;
import org.example.services.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetAllUsers
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserListDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetUserDetailsById
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserListDto> getUserDetailsById(@PathVariable Long id) {
        Optional<UserListDto> userOpt = userService.getUserDetailsById(id);
        return userOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @CreateUser
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserAfterCreationDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        UserAfterCreationDto createdUser = userService.createUser(userCreateDto);
        return ResponseEntity.status(201).body(createdUser);
    }

    @UpdateUser
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserAfterUpdateDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        String email = SecurityUtils.getCurrentUserEmail();
        User currentUser = userService.getUserByEmail(email);

        Optional<UserAfterUpdateDto> updatedOpt = userService.updateUser(id, userUpdateDto, currentUser);
        return updatedOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteUser
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        String email = SecurityUtils.getCurrentUserEmail();
        User currentUser = userService.getUserByEmail(email);

        if (userService.existsById(id)) {
            userService.deleteUser(id, currentUser);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
