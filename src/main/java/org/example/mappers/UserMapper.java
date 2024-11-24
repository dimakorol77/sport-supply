package org.example.mappers;

import org.example.dto.*;
import org.example.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    // Преобразование User в UserListDto
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

    // Преобразование User в UserAfterCreationDto
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

    // Преобразование User в UserAfterUpdateDto
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

    // Преобразование UserDto в User
    public User toEntity(UserDto dto) {
        User user = new User();
        copyProperties(dto, user);
        user.setPassword(dto.getPassword());
        return user;
    }

    // Обновление User из UserDto
    public void updateEntityFromDto(UserDto dto, User user) {
        copyProperties(dto, user);
    }

    private void copyProperties(UserDto dto, User user) {
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getPhoneNumber() != null) {
            user.setPhoneNumber(dto.getPhoneNumber());
        }
    }
}
