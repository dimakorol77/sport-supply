package org.example.services.interfaces;

import org.example.dto.*;
import org.example.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    //List<User> getAllUsers();
    List<UserListDto> getAllUsers();
    Optional<User> getUserById(Long id); // Получение пользователя по ID
    Optional<UserListDto> getUserDetailsById(Long id);
//    User createUser(User user); // Создание нового пользователя
UserAfterCreationDto createUser(UserCreateDto userCreateDto);
//    Optional<User> updateUser(Long id, User user); // Обновление пользователя
Optional<UserAfterUpdateDto> updateUser(Long id, UserUpdateDto userUpdateDto);
    void deleteUser(Long id); // Удаление пользователя
}
