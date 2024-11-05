package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.exception.errorMessage.ErrorMessage;

@Data
public class UserCreateDto {
    // Данные, которые вводятся при создании нового пользователя
    @Email(message = ErrorMessage.EMAIL_FORMAT_INVALID)
    @NotBlank(message = ErrorMessage.INVALID_USER_DATA)
    private String email;

    @NotBlank(message = ErrorMessage.PASSWORD_NOT_EMPTY) //придумать длину и т д
    private String password;
    @NotBlank(message = ErrorMessage.NAME_NOT_EMPTY)
    private String name;
    private String phoneNumber;
}
