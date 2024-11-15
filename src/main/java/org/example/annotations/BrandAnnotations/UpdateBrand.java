package org.example.annotations.BrandAnnotations;

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
        summary = "Обновление бренда",
        description = "Обновляет данные бренда по указанному ID",
        tags = "Бренды",
        responses = {
                @ApiResponse(responseCode = "200", description = "Бренд успешно обновлен"),
                @ApiResponse(responseCode = "404", description = "Бренд не найден"),
                @ApiResponse(responseCode = "400", description = "Некорректные данные")
        }
)
public @interface UpdateBrand {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
