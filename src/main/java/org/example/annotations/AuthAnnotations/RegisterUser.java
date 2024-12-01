package org.example.annotations.AuthAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST)
@Operation(
        summary = "User registration",
        description = "Registers a new user",
        tags = {"Authentication"},
        responses = {
                @ApiResponse(responseCode = "201", description = "User successfully registered"),
                @ApiResponse(responseCode = "400", description = "Invalid request data"),
                @ApiResponse(responseCode = "409", description = "User already exists")
        }
)
public @interface RegisterUser {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/register"};
}