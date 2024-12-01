package org.example.controllers;

import jakarta.validation.Valid;


import org.example.annotations.UserAnnotations.*;
import org.example.dto.*;
import org.example.enums.Role;
import org.example.exceptions.errorMessage.ErrorMessage;
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserListDto> getUserDetailsById(@PathVariable Long id) {
        UserListDto userDto = userService.getUserDetailsById(id);
        return ResponseEntity.ok(userDto);
    }

    @CreateUser
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserAfterCreationDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserAfterCreationDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(201).body(createdUser);
    }

    @UpdateUser
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserAfterUpdateDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        UserAfterUpdateDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteUser
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
