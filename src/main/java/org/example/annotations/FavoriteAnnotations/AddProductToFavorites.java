package org.example.annotations.FavoriteAnnotations;

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
        summary = "Add an item to favorites",
        description = "Adds an item to the user's favorites",
        tags = {"Favorites"},
        responses = {
                @ApiResponse(responseCode = "201", description = "Item added to favorites"),
                @ApiResponse(responseCode = "404", description = "User or item not found"),
                @ApiResponse(responseCode = "409", description = "Item is already in favorites"),
                @ApiResponse(responseCode = "403", description = "Access denied")
        }
)
public @interface AddProductToFavorites {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{productId}"};

}