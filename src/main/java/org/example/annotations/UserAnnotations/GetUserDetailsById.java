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
@RequestMapping(method = RequestMethod.GET)
@Operation(
        summary = "Получение детализированных данных пользователя по ID",
        description = "Возвращает детализированную информацию (не все поля) о пользователе с указанным ID",
        tags = {"Пользователи"},
        responses = {
                @ApiResponse(responseCode = "200", description = "Пользователь найден"),
                @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
                @ApiResponse(responseCode = "403", description = "У вас нет доступа")
        }
)
public @interface GetUserDetailsById {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}/details"};
}
