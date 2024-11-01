package org.example.services.interfaces;

import org.example.dto.UserAfterCreationDto;
import org.example.dto.UserAfterUpdateDto;
import org.example.dto.UserCreateDto;
import org.example.dto.UserUpdateDto;
import org.example.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id); // Получение пользователя по ID
//    User createUser(User user); // Создание нового пользователя
UserAfterCreationDto createUser(UserCreateDto userCreateDto);
//    Optional<User> updateUser(Long id, User user); // Обновление пользователя
Optional<UserAfterUpdateDto> updateUser(Long id, UserUpdateDto userUpdateDto);
    void deleteUser(Long id); // Удаление пользователя
}
