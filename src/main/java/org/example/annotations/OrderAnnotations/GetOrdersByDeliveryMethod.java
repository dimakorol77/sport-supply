package org.example.annotation.orderController;

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
@RequestMapping(method = RequestMethod.GET)
@Operation(
        summary = "Получение заказов по методу доставки",
        description = "Возвращает список заказов с указанным методом доставки",
        tags = "Заказы",
        responses = {
                @ApiResponse(responseCode = "200", description = "Список заказов получен"),
                @ApiResponse(responseCode = "400", description = "Некорректный метод доставки")
        }
)
@SecurityRequirement(name = "safety requirements")
public @interface GetOrdersByDeliveryMethod {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/delivery-method"};
}