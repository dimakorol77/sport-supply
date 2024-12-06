package org.example.annotations.ReviewAnnotations;


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
        summary = "Creating a new review",
        description = "Creates a new review",
        tags = "Reviews",
        responses = {
                @ApiResponse(responseCode = "201", description = "Review successfully created"),
                @ApiResponse(responseCode = "400", description = "Invalid data")
        }
)
public @interface CreateReview {

}
