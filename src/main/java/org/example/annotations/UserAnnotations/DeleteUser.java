package org.example.annotations.UserAnnotations;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.DELETE)
@Operation(
        summary = "Deleting a user",
        description = "Deletes a user by the specified ID",
        tags = {"Users"},
        responses = {
                @ApiResponse(responseCode = "204", description = "User successfully deleted"),
                @ApiResponse(responseCode = "404", description = "User not found"),
                @ApiResponse(responseCode = "403", description = "Access denied")
        }
)
public @interface DeleteUser {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
