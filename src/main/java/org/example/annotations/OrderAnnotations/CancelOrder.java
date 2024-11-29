package org.example.annotations.OrderAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.DELETE) // Изменено с POST на DELETE
@Operation(
        summary = "Отмена заказа",
        description = "Отменяет заказ по его ID",
        tags = "Заказы",
        responses = {
                @ApiResponse(responseCode = "200", description = "Заказ отменен"),
                @ApiResponse(responseCode = "400", description = "Заказ не может быть отменен"),
                @ApiResponse(responseCode = "404", description = "Заказ не найден"),
                @ApiResponse(responseCode = "403", description = "У вас нет доступа")
        }
)
public @interface CancelOrder {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{orderId}/cancel"};
}
