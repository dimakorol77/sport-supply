package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.dto.*;
import org.example.models.User;
import org.example.services.impl.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private  UserServiceImp userServiceImp;


    // для получения всех пользователей
    @GetMapping
    @Operation(summary = "Получение всех пользователей",
            description = "Возвращает список всех пользователей",
            tags = "Пользователи",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователи найдены")
            },
            hidden = false
    )
//    public List<User> getAllUsers() {
//        return userServiceImp.getAllUsers();
//    }
    public List<UserListDto> getAllUsers() {
        return userServiceImp.getAllUsers().stream()
                .map(user -> new UserListDto(user.getId(), user.getName(), user.getEmail(), user.getPhoneNumber()))
                .collect(Collectors.toList());
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
        Optional<UserListDto> userOpt = userServiceImp.getUserDetailsById(id);
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
        Optional<User> userOpt = userServiceImp.getUserById(id);
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
//    public ResponseEntity<User> createUser(@RequestBody User user) {
//        User created = userServiceImp.createUser(user);
//        return ResponseEntity.status(201).body(created);
//    }
    public ResponseEntity<UserAfterCreationDto> createUser(@RequestBody UserCreateDto userCreateDto) {
        UserAfterCreationDto createdUser = userServiceImp.createUser(userCreateDto);
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
//    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
//        Optional<User> updatedOpt = userServiceImp.updateUser(id, updatedUser);
//        return updatedOpt.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
    public ResponseEntity<UserAfterUpdateDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto) {
        Optional<UserAfterUpdateDto> updatedOpt = userServiceImp.updateUser(id, userUpdateDto);
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
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        userServiceImp.deleteUser(id);
//        return ResponseEntity.noContent().build();
//    }
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userServiceImp.existsById(id)) { // Проверка существования пользователя
            userServiceImp.deleteUser(id);
            return ResponseEntity.noContent().build(); // Возвращает 204 если пользователь был удален
        } else {
            return ResponseEntity.notFound().build(); // Возвращает 404 если пользователь не найден
        }
    }
}

