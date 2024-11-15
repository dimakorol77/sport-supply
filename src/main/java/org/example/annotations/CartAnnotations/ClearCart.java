<<<<<<<< HEAD:src/main/java/org/example/annotation/cartController/ClearCart.java
package org.example.annotation.cartController;
========
package org.example.annotations;
>>>>>>>> development:src/main/java/org/example/annotations/ClearCart.java

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // Указывает, что данная аннотация может быть использована только для методов.
@Retention(RetentionPolicy.RUNTIME) // Аннотация будет доступна во время выполнения программы.
@RequestMapping(method = RequestMethod.DELETE) // Указывает, что аннотированный метод обрабатывает HTTP DELETE запросы.
@Operation( // Аннотация из библиотеки Springdoc для генерации документации OpenAPI.
        summary = "Очистка корзины", // Краткое описание операции.
        description = "Удаляет все товары из корзины", // Подробное описание, что делает этот метод.
        tags = "Корзина", // Теги для группировки методов в документации.
        responses = { // Описание возможных ответов от сервера.
                @ApiResponse(responseCode = "204", description = "Корзина успешно очищена"), // Ответ при успешном выполнении (статус 204).
                @ApiResponse(responseCode = "404", description = "Корзина не найдена") // Ответ при ошибке (статус 404, если корзина не найдена).
        }
)
public @interface ClearCart {// Объявление кастомной аннотации ClearCart.
    @AliasFor(annotation = RequestMapping.class, attribute = "path")// Позволяет задавать значение для параметра "path" в аннотации RequestMapping.
    String[] path() default {"/{cartId}/clear"};// Определяет путь URL, по которому будет выполняться запрос (например, /{cartId}/clear).
}
