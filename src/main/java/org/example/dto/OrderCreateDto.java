package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.example.enums.DeliveryMethod;

@Data
public class OrderCreateDto {

    @NotNull(message = "Delivery method cannot be null")
    private DeliveryMethod deliveryMethod;

    @NotNull(message = "Delivery address cannot be null")
    @Size(min = 5, message = "Delivery address must be at least 5 characters long")
    private String deliveryAddress;

    @NotNull(message = "Contact information cannot be null")
    @Size(min = 5, message = "Contact information must be at least 5 characters long")
    private String contactInfo;


}
