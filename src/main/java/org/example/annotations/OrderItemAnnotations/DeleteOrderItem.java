<<<<<<<< HEAD:src/main/java/org/example/annotation/orderItem/DeleteOrderItem.java
package org.example.annotation.orderItem;
========
package org.example.annotations;
>>>>>>>> development:src/main/java/org/example/annotations/DeleteOrderItem.java

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
        summary = "Удаление элемента заказа",
        description = "Удаляет указанный элемент заказа",
        tags = {"Элементы заказа"},
        responses = {
                @ApiResponse(responseCode = "204", description = "Элемент заказа успешно удален"),
                @ApiResponse(responseCode = "404", description = "Элемент заказа не найден")
        }
)
public @interface DeleteOrderItem {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{orderItemId}"};
}
