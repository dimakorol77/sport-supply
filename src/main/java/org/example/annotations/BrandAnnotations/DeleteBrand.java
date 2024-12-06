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
        summary = "Removing a brand",
        description = "Deletes a brand by the specified ID",
        tags = "Brands",
        responses = {
                @ApiResponse(responseCode = "204", description = "Brand removed successfully"),
                @ApiResponse(responseCode = "404", description = "Brand not found")
        }
)
public @interface DeleteBrand {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}

