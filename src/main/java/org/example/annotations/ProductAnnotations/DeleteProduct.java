package org.example.annotations.ProductAnnotations;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.DELETE)
@Operation(
        summary = "Удаление продукта",
        description = "Удаляет продукт по указанному ID",
        tags = "Продукты",
        responses = {
                @ApiResponse(responseCode = "204", description = "Продукт успешно удален"),
                @ApiResponse(responseCode = "404", description = "Продукт не найден")
        }
)
public @interface DeleteProduct {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
