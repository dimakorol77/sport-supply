package org.example.annotations.AuthAnnotations;

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
@RequestMapping(method = RequestMethod.POST)
@Operation(
        summary = "User authentication",
        description = "Authenticates the user and returns a JWT token",
        tags = {"Authentication"},
        responses = {
                @ApiResponse(responseCode = "200", description = "User successfully authenticated"),
                @ApiResponse(responseCode = "401", description = "Invalid credentials")
        }
)
public @interface AuthenticateUser {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/login"};
}
