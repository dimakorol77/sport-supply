package org.example.annotations.ProductAnnotations;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST)
@Operation(
        summary = "Создание нового продукта",
        description = "Создает новый продукт",
        tags = "Продукты",
        responses = {
                @ApiResponse(responseCode = "201", description = "Продукт успешно создан"),
                @ApiResponse(responseCode = "400", description = "Некорректные данные")
        }
)
public @interface CreateProduct {

}
