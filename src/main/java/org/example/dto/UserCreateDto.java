package org.example.dto;

import lombok.Data;

@Data
public class UserCreateDto {
    // Данные, которые вводятся при создании нового пользователя
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
}
