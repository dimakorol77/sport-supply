package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.exceptions.errorMessage.ErrorMessage;

@Data
public class UserDto {
    // Данные, которые вводятся при создании нового пользователя
    @Email(message = ErrorMessage.EMAIL_FORMAT_INVALID)
    @NotBlank(message = ErrorMessage.INVALID_USER_DATA)
    private String email;

    @NotBlank(message = ErrorMessage.PASSWORD_NOT_EMPTY)
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String password;
    @NotBlank(message = ErrorMessage.NAME_NOT_EMPTY)
    private String name;

    private String phoneNumber;
}
