package org.example.services.impl;

import org.example.dto.*;
import org.example.enums.Role;
import org.example.exception.IdNotFoundException;
import org.example.exception.UserAlreadyExistsException;
import org.example.exception.errorMessage.ErrorMessage;
import org.example.mappers.UserMapper;
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
    private final UserMapper userMapper;

    // Используем конструкторную инъекцию
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // Получить всех пользователей
    @Override
    public List<UserListDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserListDto) // Использование правильного метода маппера
                .collect(Collectors.toList());
    }

    // Получить пользователя по ID
    @Override
    public Optional<User> getUserById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new IdNotFoundException(ErrorMessage.ID_NOT_FOUND);
        }
        return userOpt;
    }

    // Получить детализированные данные пользователя по ID
    @Override
    public Optional<UserListDto> getUserDetailsById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new IdNotFoundException(ErrorMessage.ID_NOT_FOUND);
        }
        return userOpt.map(userMapper::toUserListDto); // Использование правильного метода маппера
    }

    // Создать нового пользователя
    @Override
    public UserAfterCreationDto createUser(UserCreateDto userCreateDto) {
        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new UserAlreadyExistsException(ErrorMessage.USER_ALREADY_EXISTS);
        }
        User user = userMapper.toEntity(userCreateDto); // Использование метода для преобразования из DTO в сущность
        user.setRole(Role.USER); // Роль по умолчанию
        User createdUser = userRepository.save(user);

        return userMapper.toUserAfterCreationDto(createdUser); // Преобразование сущности в DTO
    }

    // Обновить пользователя
    @Override
    public Optional<UserAfterUpdateDto> updateUser(Long id, UserUpdateDto userUpdateDto) {
        return userRepository.findById(id).map(user -> {
            userMapper.updateEntityFromDto(userUpdateDto, user); // Обновляем сущность с помощью маппера
            user.setUpdatedAt(LocalDateTime.now());
            User updatedUser = userRepository.save(user);

            return userMapper.toUserAfterUpdateDto(updatedUser); // Преобразование сущности в DTO
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
        if (!existsById(id)) {
            throw new IdNotFoundException(ErrorMessage.ID_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }
}
