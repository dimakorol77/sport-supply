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
    Optional<UserAfterUpdateDto> updateUser(Long id, UserUpdateDto userUpdateDto, User currentUser);
    void deleteUser(Long id, User currentUser);
    boolean existsById(Long id);
    User getUserByEmail(String email);
}
