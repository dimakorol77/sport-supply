package org.example.annotation;

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
// Аннотация доступна в процессе выполнения
@Retention(RetentionPolicy.RUNTIME)
// Аннотация включает в себя метод GET и указывает путь
@RequestMapping(method = RequestMethod.GET)
@Operation(
        summary = "Получение всех заказов", // Описание запроса
        description = "Возвращает список всех заказов", // Подробное описание операции
        tags = {"Заказы"}, // Теги для группировки в Swagger UI
        responses = { // Описание возможных ответов
                @ApiResponse(responseCode = "200", description = "Заказы найдены") // Ответ 200 с описанием
        }
)
public @interface GetAllOrders { // Объявление кастомной аннотации `@GetAllOrders`
    // Позволяет указывать путь при использовании аннотации
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/api/orders"};
}