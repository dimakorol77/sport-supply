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
@RequestMapping(method = RequestMethod.DELETE)
@Operation(
        summary = "Remove an item from favorites",
        description = "Removes an item from the user's favorites",
        tags = {"Favorites"},
        responses = {
                @ApiResponse(responseCode = "204", description = "Item removed from favorites"),
                @ApiResponse(responseCode = "404", description = "Item not found in favorites"),
                @ApiResponse(responseCode = "403", description = "Access denied")
        }
)
public @interface RemoveProductFromFavorites {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/remove/{productId}"};
}
