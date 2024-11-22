package org.example.annotations.ProductAnnotations;

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
        summary = "Фильтрация и сортировка продуктов",
        description = "Позволяет фильтровать и сортировать продукты по различным критериям",
        tags = {"Продукты"},
        responses = {
                @ApiResponse(responseCode = "200", description = "Продукты получены"),
                @ApiResponse(responseCode = "400", description = "Неверные параметры запроса"),
                @ApiResponse(responseCode = "403", description = "У вас нет доступа")
        }
)
public @interface FilterAndSortProducts {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/filter"};
}
