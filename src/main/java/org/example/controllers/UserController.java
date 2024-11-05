package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.example.dto.*;
import org.example.models.User;
import org.example.services.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // Используем конструкторную инъекцию
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Получить всех пользователей
    @GetMapping
    @Operation(summary = "Получение всех пользователей",
            description = "Возвращает список всех пользователей",
            tags = "Пользователи",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователи найдены")
            }
    )
    public List<UserListDto> getAllUsers() {
        return userService.getAllUsers();
    }

    // Получить детализированные данные пользователя по ID
    @GetMapping("/{id}/details")
    @Operation(summary = "Получение детализированных данных пользователя по ID",
            description = "Возвращает детализированную информацию (не все поля) о пользователе с указанным ID",
            tags = "Пользователи",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь найден"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            })
    public ResponseEntity<UserListDto> getUserDetailsById(@PathVariable Long id) {
        Optional<UserListDto> userOpt = userService.getUserDetailsById(id);
        return userOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Получить пользователя по ID
    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя по ID",
            description = "Возвращает всю информацию о пользователе с указанным ID",
            tags = "Пользователи",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь найден"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            })
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.getUserById(id);
        return userOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Создать нового пользователя
    @PostMapping
    @Operation(summary = "Создание нового пользователя",
            tags = "Пользователи",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    public ResponseEntity<UserAfterCreationDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        UserAfterCreationDto createdUser = userService.createUser(userCreateDto);
        return ResponseEntity.status(201).body(createdUser);
    }

    // Обновить пользователя
    @PutMapping("/{id}")
    @Operation(summary = "Обновление пользователя",
            description = "Обновляет пользователя по указанному ID",
            tags = "Пользователи",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            })
    public ResponseEntity<UserAfterUpdateDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        Optional<UserAfterUpdateDto> updatedOpt = userService.updateUser(id, userUpdateDto);
        return updatedOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Удалить пользователя
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя",
            description = "Удаляет пользователя по указанному ID",
            tags = "Пользователи",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Пользователь успешно удален"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.existsById(id)) { // Проверка существования пользователя
            userService.deleteUser(id);
            return ResponseEntity.noContent().build(); // Возвращаем статус 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // Возвращаем статус 404 Not Found
        }
    }
}
