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
@RequestMapping(method = RequestMethod.DELETE)
@Operation(
        summary = "Удаление пользователя",
        description = "Удаляет пользователя по указанному ID",
        tags = {"Пользователи"},
        responses = {
                @ApiResponse(responseCode = "204", description = "Пользователь успешно удален"),
                @ApiResponse(responseCode = "404", description = "Пользователь не найден")
        }
)
public @interface DeleteUser {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
