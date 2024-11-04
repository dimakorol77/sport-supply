package org.example.services.impl;

import org.example.dto.*;
import org.example.enums.Role;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.services.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // Используем конструкторную инъекцию
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Получить всех пользователей
    @Override
    public List<UserListDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserListDto(user.getId(), user.getName(), user.getEmail(), user.getPhoneNumber()))
                .collect(Collectors.toList());
    }

    // Получить пользователя по ID
    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Получить детализированные данные пользователя по ID
    @Override
    public Optional<UserListDto> getUserDetailsById(Long id) {
        return userRepository.findById(id).map(user -> new UserListDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber()
        ));
    }

    // Создать нового пользователя
    @Override
    public UserAfterCreationDto createUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setEmail(userCreateDto.getEmail());
        user.setPassword(userCreateDto.getPassword());
        user.setName(userCreateDto.getName());
        user.setPhoneNumber(userCreateDto.getPhoneNumber());

        if (user.getRole() == null) {
            user.setRole(Role.USER); // Роль по умолчанию
        }

        User createdUser = userRepository.save(user);

        return UserAfterCreationDto.builder()
                .id(createdUser.getId())
                .email(createdUser.getEmail())
                .name(createdUser.getName())
                .phoneNumber(createdUser.getPhoneNumber())
                .role(createdUser.getRole())
                .createdAt(createdUser.getCreatedAt())
                .build();
    }

    // Обновить пользователя
    @Override
    public Optional<UserAfterUpdateDto> updateUser(Long id, UserUpdateDto userUpdateDto) {
        return userRepository.findById(id).map(user -> {
            user.setEmail(userUpdateDto.getEmail());
            user.setName(userUpdateDto.getName());
            user.setPhoneNumber(userUpdateDto.getPhoneNumber());

            if (userUpdateDto.getPassword() != null) {
                user.setPassword(userUpdateDto.getPassword()); // Обновляем пароль
            }
            user.setUpdatedAt(LocalDateTime.now());

            User updatedUser = userRepository.save(user);

            return UserAfterUpdateDto.builder()
                    .id(updatedUser.getId())
                    .email(updatedUser.getEmail())
                    .name(updatedUser.getName())
                    .updatedAt(updatedUser.getUpdatedAt())
                    .build();
        });
    }

    // Проверка, существует ли пользователь по ID
    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    // Удалить пользователя
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
