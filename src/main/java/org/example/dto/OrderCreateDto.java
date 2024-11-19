package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.example.enums.DeliveryMethod;

@Data
public class OrderCreateDto {
    //информация, необходимая для создания нового заказа.
    @NotNull(message = "Delivery method cannot be null")
    private DeliveryMethod deliveryMethod; // Метод доставки

    @NotNull(message = "Delivery address cannot be null")
    @Size(min = 5, message = "Delivery address must be at least 5 characters long")
    private String deliveryAddress; // Адрес доставки

    @NotNull(message = "Contact information cannot be null")
    @Size(min = 5, message = "Contact information must be at least 5 characters long")
    private String contactInfo; // Контактная информация


}
