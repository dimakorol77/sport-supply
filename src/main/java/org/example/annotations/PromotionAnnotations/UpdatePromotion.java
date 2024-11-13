package org.example.annotations.PromotionAnnotations;



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
        summary = "Обновление акции",
        description = "Обновляет акцию по указанному ID",
        tags = "Акции",
        responses = {
                @ApiResponse(responseCode = "200", description = "Акция успешно обновлена"),
                @ApiResponse(responseCode = "404", description = "Акция не найдена"),
                @ApiResponse(responseCode = "400", description = "Некорректные данные")
        }
)
public @interface UpdatePromotion {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
