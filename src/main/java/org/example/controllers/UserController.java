package org.example.controllers;

import jakarta.validation.Valid;

import org.example.annotation.user.*;

import org.example.dto.*;
import org.example.models.User;
import org.example.services.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    // Используем конструкторную инъекцию
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Получить всех пользователей
    @GetAllUsers
    public List<UserListDto> getAllUsers() {
        return userService.getAllUsers(); // Вызов метода с правильным маппингом
    }

    // Получить детализированные данные пользователя по ID
    @GetUserDetailsById
    public ResponseEntity<UserListDto> getUserDetailsById(@PathVariable Long id) {
        Optional<UserListDto> userOpt = userService.getUserDetailsById(id);
        return userOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Получить пользователя по ID
    @GetUserById
    public ResponseEntity<UserListDto> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.getUserById(id); // Получаем сущность
        return userOpt.map(user -> ResponseEntity.ok(userService.getUserDetailsById(id).orElse(null))) // Преобразуем в DTO
                .orElse(ResponseEntity.notFound().build());
    }

    // Создать нового пользователя
    @CreateUser
    public ResponseEntity<UserAfterCreationDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        UserAfterCreationDto createdUser = userService.createUser(userCreateDto);
        return ResponseEntity.status(201).body(createdUser); // Возвращаем созданного пользователя
    }

    // Обновить пользователя
    @UpdateUser
    public ResponseEntity<UserAfterUpdateDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        Optional<UserAfterUpdateDto> updatedOpt = userService.updateUser(id, userUpdateDto);
        return updatedOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Удалить пользователя
    @DeleteUser
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.existsById(id)) { // Проверка существования пользователя
            userService.deleteUser(id);
            return ResponseEntity.noContent().build(); // Возвращаем статус 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // Возвращаем статус 404 Not Found
        }
    }
}
