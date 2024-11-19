package org.example.annotations.PaymentAnnotations;

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
@RequestMapping(method = RequestMethod.PATCH)
@Operation(
        summary = "Обновление статуса платежа",
        description = "Обновляет статус существующего платежа по его идентификатору",
        tags = {"Платежи"},
        responses = {
                @ApiResponse(responseCode = "200", description = "Статус платежа успешно обновлен"),
                @ApiResponse(responseCode = "404", description = "Платеж не найден"),
                @ApiResponse(responseCode = "403", description = "У вас нет доступа")
        }
)
public @interface UpdatePaymentStatus {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{paymentId}/status"};
}
