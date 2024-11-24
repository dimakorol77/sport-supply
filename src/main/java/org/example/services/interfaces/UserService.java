package org.example.services.interfaces;

import org.example.dto.*;
import org.example.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserListDto> getAllUsers();
    UserListDto getUserDetailsById(Long id);
    UserAfterCreationDto createUser(UserDto userDto);
    UserAfterUpdateDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    boolean existsById(Long id);
    User getUserByEmail(String email);
}
