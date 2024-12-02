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
        summary = "Brand update",
        description = "Updates brand data for the specified ID",
        tags = "Brands",
        responses = {
                @ApiResponse(responseCode = "200", description = "The brand has been successfully updated"),
                @ApiResponse(responseCode = "404", description = "Brand not found"),
                @ApiResponse(responseCode = "400", description = "Incorrect data")
        }
)
public @interface UpdateBrand {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}

