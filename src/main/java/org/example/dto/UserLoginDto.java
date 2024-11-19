package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDto {
    //обработка данных при логине
    @Email(message = "Неверный формат электронной почты")
    @NotBlank(message = "Электронная почта не должна быть пустой")
    private String email;

    @NotBlank(message = "Пароль не должен быть пустым")
    private String password;
}
