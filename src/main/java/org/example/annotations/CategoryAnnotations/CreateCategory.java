package org.example.annotations.CategoryAnnotations;

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
        summary = "Создание новой категории",
        description = "Создает новую категорию",
        tags = "Категории",
        responses = {
                @ApiResponse(responseCode = "201", description = "Категория успешно создана"),
                @ApiResponse(responseCode = "400", description = "Некорректные данные")
        }
)
public @interface CreateCategory {

}
