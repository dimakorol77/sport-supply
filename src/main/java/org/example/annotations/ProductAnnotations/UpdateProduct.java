package org.example.annotations.ProductAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.PUT)
@Operation(
        summary = "Обновление продукта",
        description = "Обновляет продукт по указанному ID",
        tags = "Продукты",
        responses = {
                @ApiResponse(responseCode = "200", description = "Продукт успешно обновлен"),
                @ApiResponse(responseCode = "404", description = "Продукт не найден"),
                @ApiResponse(responseCode = "400", description = "Некорректные данные")
        }
)
public @interface UpdateProduct {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
