package org.example.annotations.ImageAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.GET)
@Operation(summary = "Get images by product ID", description = "Retrieves a list of images associated with a product")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Images retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
})
public @interface GetImagesByProductId {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/product/{productId}"};

//    @Parameter(description = "ID of the product", required = true)
//    String productIdParam() default "productId";
}
