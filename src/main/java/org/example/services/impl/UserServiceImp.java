package org.example.services.impl;

import org.example.dto.UserAfterCreationDto;
import org.example.dto.UserAfterUpdateDto;
import org.example.dto.UserCreateDto;
import org.example.dto.UserUpdateDto;
import org.example.enums.Role;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // найти всех пользователей
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    // Получить пользователя по ID
    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id); // Возвращаем null, если пользователь не найден
    }

    // Создать нового пользователя
    @Override
//    public User createUser(User user) {
//        if (user.getRole() == null) {
//            user.setRole(Role.USER); // Назначаем роль по умолчанию
//        }
//        return userRepository.save(user);
//    }
    public UserAfterCreationDto createUser(UserCreateDto userCreateDto) {
        User user = new User(); // Создаем новый объект User
        user.setEmail(userCreateDto.getEmail());
        user.setPassword(userCreateDto.getPassword());
        user.setName(userCreateDto.getName());
        user.setPhoneNumber(userCreateDto.getPhoneNumber());
        user.setCreatedAt(LocalDateTime.now());

        if (user.getRole() == null) {
            user.setRole(Role.USER); // Назначаем роль по умолчанию
        }

        User createdUser = userRepository.save(user);

        // Преобразуем User в UserAfterCreationDto для возврата
        return UserAfterCreationDto.builder()
                .id(createdUser.getId())
                .email(createdUser.getEmail())
                .name(createdUser.getName())
                .phoneNumber(createdUser.getPhoneNumber())
                .role(createdUser.getRole())
                .createdAt(createdUser.getCreatedAt())
                .build();
    }

    @Override
//    public Optional<User> updateUser(Long id, User updatedUser) {
//        return userRepository.findById(id).map(user -> {
//            // Обновление полей пользователя
//            user.setEmail(updatedUser.getEmail());
//            user.setPassword(updatedUser.getPassword()); //  пароль
//            user.setName(updatedUser.getName());
//            user.setPhoneNumber(updatedUser.getPhoneNumber());
//            user.setRole(updatedUser.getRole());
//
//            // Обновление времени последнего изменения
//            user.setUpdatedAt(LocalDateTime.now());
//
//            // Сохранение обновленного пользователя
//            return userRepository.save(user);
//        });
//    }
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

    // Удалить пользователя
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }



}
