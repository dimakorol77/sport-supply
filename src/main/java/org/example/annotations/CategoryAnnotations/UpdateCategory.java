package org.example.annotations.CategoryAnnotations;



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
        summary = "Category update",
        description = "Updates category data by specified ID",
        tags = "Categories",
        responses = {
                @ApiResponse(responseCode = "200", description = "Category updated successfully"),
                @ApiResponse(responseCode = "404", description = "Category not found"),
                @ApiResponse(responseCode = "400", description = "Incorrect data")
        }
)
public @interface UpdateCategory {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
