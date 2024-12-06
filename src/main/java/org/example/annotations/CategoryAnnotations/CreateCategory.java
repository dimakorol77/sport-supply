package org.example.annotations.CategoryAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST)
@Operation(
        summary = "Create a new category",
        description = "Creates a new category",
        tags = "Categories",
        responses = {
                @ApiResponse(responseCode = "201", description = "Category successfully created"),
                @ApiResponse(responseCode = "400", description = "Incorrect data")
        }
)
public @interface CreateCategory {

}
