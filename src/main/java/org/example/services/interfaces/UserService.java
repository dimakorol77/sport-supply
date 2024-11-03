package org.example.services.interfaces;

import org.example.dto.*;
import org.example.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserListDto> getAllUsers();
    Optional<User> getUserById(Long id);
    Optional<UserListDto> getUserDetailsById(Long id);
    UserAfterCreationDto createUser(UserCreateDto userCreateDto);
    Optional<UserAfterUpdateDto> updateUser(Long id, UserUpdateDto userUpdateDto);
    void deleteUser(Long id);
    boolean existsById(Long id); // Добавили этот метод
}
