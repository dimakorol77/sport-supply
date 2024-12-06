package org.example.annotations.ReviewAnnotations;



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
        summary = "Deleting a review",
        description = "Deletes a review by the specified ID",
        tags = "Reviews",
        responses = {
                @ApiResponse(responseCode = "204", description = "Review successfully deleted"),
                @ApiResponse(responseCode = "404", description = "Review not found")
        }
)
public @interface DeleteReview {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
