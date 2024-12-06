package org.example.annotations.FavoriteAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.GET)
@Operation(
        summary = "Getting user's favorite items (admin only)",
        description = "The administrator can retrieve a list of favorite items for any user by their ID",
        tags = {"Favorites"},
        responses = {
                @ApiResponse(responseCode = "200", description = "Favorite items list retrieved"),
                @ApiResponse(responseCode = "403", description = "Access denied"),
                @ApiResponse(responseCode = "404", description = "User not found")
        }
)
public @interface GetUserFavoritesByAdmin {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{userId}"};
}
