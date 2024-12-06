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
@RequestMapping(method = RequestMethod.PUT)
@Operation(
        summary = "Updating a user",
        description = "Updates the user with the specified ID",
        tags = {"Users"},
        responses = {
                @ApiResponse(responseCode = "200", description = "User successfully updated"),
                @ApiResponse(responseCode = "404", description = "User not found"),
                @ApiResponse(responseCode = "400", description = "Invalid data"),
                @ApiResponse(responseCode = "403", description = "You are not an admin, access denied")
        }
)
public @interface UpdateUser {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
