package org.example.annotations.PromotionAnnotations;



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
        summary = "Удаление акции",
        description = "Удаляет акцию по указанному ID",
        tags = "Акции",
        responses = {
                @ApiResponse(responseCode = "204", description = "Акция успешно удалена"),
                @ApiResponse(responseCode = "404", description = "Акция не найдена")
        }
)
public @interface DeletePromotion {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
