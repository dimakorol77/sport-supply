<<<<<<<< HEAD:src/main/java/org/example/annotation/user/CreateUser.java
package org.example.annotation.user;
========
package org.example.annotations;
>>>>>>>> development:src/main/java/org/example/annotations/CreateUser.java

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST)
@Operation(
        summary = "Создание нового пользователя",
        tags = {"Пользователи"},
        responses = {
                @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
                @ApiResponse(responseCode = "400", description = "Некорректные данные")
        }
)
public @interface CreateUser {
}
