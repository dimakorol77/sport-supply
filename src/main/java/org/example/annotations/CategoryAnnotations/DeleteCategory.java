package org.example.annotations.CategoryAnnotations;



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
        summary = "Удаление категории",
        description = "Удаляет категорию по указанному ID",
        tags = "Категории",
        responses = {
                @ApiResponse(responseCode = "204", description = "Категория успешно удалена"),
                @ApiResponse(responseCode = "404", description = "Категория не найдена")
        }
)
public @interface DeleteCategory {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
