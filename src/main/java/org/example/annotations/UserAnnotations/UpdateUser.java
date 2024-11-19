package org.example.annotations.UserAnnotations;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.PUT)
@Operation(
        summary = "Обновление пользователя",
        description = "Обновляет пользователя по указанному ID",
        tags = {"Пользователи"},
        responses = {
                @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен"),
                @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
                @ApiResponse(responseCode = "400", description = "Некорректные данные"),
                @ApiResponse(responseCode = "403", description = "Вы не админ, у вас нет доступа")
        }
)
public @interface UpdateUser {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
