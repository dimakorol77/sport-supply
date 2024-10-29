package org.example.controllers;

import org.example.models.User;
import org.example.services.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // для получения всех пользователей
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
