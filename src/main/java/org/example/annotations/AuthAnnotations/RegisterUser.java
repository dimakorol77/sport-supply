package org.example.annotations.AuthAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST)
@Operation(
        summary = "Регистрация пользователя",
        description = "Регистрирует нового пользователя",
        tags = {"Аутентификация"},
        responses = {
                @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
                @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
                @ApiResponse(responseCode = "409", description = "Пользователь уже существует")
        }
)
public @interface RegisterUser {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/register"};
}