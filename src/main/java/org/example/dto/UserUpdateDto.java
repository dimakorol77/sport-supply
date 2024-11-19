package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.exceptions.errorMessage.ErrorMessage;

@Data
public class UserUpdateDto {
    //данные, которые могут быть обновлены для существующего пользователя
    @Email(message = ErrorMessage.EMAIL_FORMAT_INVALID)
    @NotBlank(message = ErrorMessage.INVALID_USER_DATA)
    private String email;
    @NotBlank(message = ErrorMessage.NAME_NOT_EMPTY)
    private String name;
    private String phoneNumber;
    @NotBlank(message = ErrorMessage.PASSWORD_NOT_EMPTY) //придумать длину и т д
    private String password;
}
