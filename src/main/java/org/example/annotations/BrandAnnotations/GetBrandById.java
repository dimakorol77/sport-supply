package org.example.annotations.BrandAnnotations;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.GET)
@Operation(
        summary = "Получение бренда по ID",
        description = "Возвращает бренд с указанным ID",
        tags = "Бренды",
        responses = {
                @ApiResponse(responseCode = "200", description = "Бренд найден"),
                @ApiResponse(responseCode = "404", description = "Бренд не найден")
        }
)
public @interface GetBrandById {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
