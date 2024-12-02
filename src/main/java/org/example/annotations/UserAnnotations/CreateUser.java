package org.example.annotations.UserAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST)
@Operation(
        summary = "Creating a new user",
        tags = {"Users"},
        responses = {
                @ApiResponse(responseCode = "201", description = "User successfully created"),
                @ApiResponse(responseCode = "400", description = "Invalid data"),
                @ApiResponse(responseCode = "403", description = "Access denied")
        }
)
public @interface CreateUser {
}
