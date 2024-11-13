package org.example.annotations.BrandAnnotations;



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
        summary = "Удаление бренда",
        description = "Удаляет бренд по указанному ID",
        tags = "Бренды",
        responses = {
                @ApiResponse(responseCode = "204", description = "Бренд успешно удален"),
                @ApiResponse(responseCode = "404", description = "Бренд не найден")
        }
)
public @interface DeleteBrand {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
