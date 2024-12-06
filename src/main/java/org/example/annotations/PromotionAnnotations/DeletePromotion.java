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
        summary = "Deleting a promotion",
        description = "Deletes a promotion by the specified ID",
        tags = "Promotions",
        responses = {
                @ApiResponse(responseCode = "204", description = "Promotion successfully deleted"),
                @ApiResponse(responseCode = "404", description = "Promotion not found")
        }
)
public @interface DeletePromotion {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
