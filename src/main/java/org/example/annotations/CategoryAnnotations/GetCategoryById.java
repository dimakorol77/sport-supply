package org.example.annotations.CategoryAnnotations;


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
        summary = "Getting a category by ID",
        description = "Returns the category with the specified ID",
        tags = "Categories",
        responses = {
                @ApiResponse(responseCode = "200", description = "Category found"),
                @ApiResponse(responseCode = "404", description = "Category not found")
        }
)
public @interface GetCategoryById {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
