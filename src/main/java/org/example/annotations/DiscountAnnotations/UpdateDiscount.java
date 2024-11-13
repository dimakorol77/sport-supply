package org.example.annotations.DiscountAnnotations;



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
        summary = "Обновление скидки",
        description = "Обновляет скидку по указанному ID",
        tags = "Скидки",
        responses = {
                @ApiResponse(responseCode = "200", description = "Скидка успешно обновлена"),
                @ApiResponse(responseCode = "404", description = "Скидка не найдена"),
                @ApiResponse(responseCode = "400", description = "Некорректные данные")
        }
)
public @interface UpdateDiscount {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
