
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
        summary = "Getting all categories",
        description = "Returns a list of all categories",
        tags = "Categories",
        responses = {
                @ApiResponse(responseCode = "200", description = "Categories found")
        }
)
public @interface GetAllCategories {

}
