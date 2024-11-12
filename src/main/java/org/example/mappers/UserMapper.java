package org.example.mappers;

import org.example.dto.*;
import org.example.enums.Role;
import org.example.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    // Преобразование сущности User в DTO UserListDto
    public UserListDto toUserListDto(User user) {
        if (user == null) {
            return null;
        }
        UserListDto dto = new UserListDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        return dto;
    }

    // Преобразование сущности User в DTO UserAfterCreationDto
    public UserAfterCreationDto toUserAfterCreationDto(User user) {
        if (user == null) {
            return null;
        }
        UserAfterCreationDto dto = new UserAfterCreationDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    // Преобразование сущности User в DTO UserAfterUpdateDto
    public UserAfterUpdateDto toUserAfterUpdateDto(User user) {
        if (user == null) {
            return null;
        }
        UserAfterUpdateDto dto = new UserAfterUpdateDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    // Преобразование DTO UserCreateDto в сущность User
    public User toEntity(UserCreateDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(Role.USER); // Устанавливаем роль по умолчанию
        return user;
    }

    // Преобразование DTO UserUpdateDto в сущность User
    public User toEntity(UserUpdateDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getPassword() != null) {
            user.setPassword(dto.getPassword()); // Обновляем пароль, если он есть
        }
        return user;
    }

    // Обновление сущности User из DTO UserUpdateDto
    public void updateEntityFromDto(UserUpdateDto dto, User user) {
        if (dto == null || user == null) {
            return;
        }
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getPassword() != null) {
            user.setPassword(dto.getPassword()); // Обновляем пароль, если он есть
        }
    }
}
