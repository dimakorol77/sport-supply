package org.example.annotations.ProductAnnotations;



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
        summary = "Получение всех продуктов",
        description = "Возвращает список всех продуктов",
        tags = "Продукты",
        responses = {
                @ApiResponse(responseCode = "200", description = "Продукты найдены")
        }
)
public @interface GetAllProducts {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/"};
}
