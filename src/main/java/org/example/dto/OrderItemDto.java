package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.exception.errorMessage.ErrorMessage;
import org.example.models.OrderItem;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    @NotNull(message = "ID продукта не может быть пустым")
private Long productId;
    @NotBlank(message = "Название продукта не может быть пустым")
    private String productName;
    private String productDescription;
  //  private String productImageUrl;
    private String productCategoryName;
    @NotNull(message = "Цена не может быть пустой")
    @DecimalMin(value = "0.0", inclusive = true, message = "Цена не может быть отрицательной")
    private BigDecimal price;
    @NotNull(message = "Количество не может быть пустым")
    @Min(value = 1, message = "Количество должно быть не менее 1")
    private int quantity;

    public OrderItemDto(OrderItem orderItem) {
        this.productId = orderItem.getProductId();
        this.productName = orderItem.getProductName();
        this.productDescription = orderItem.getProductDescription();
      //  this.productImageUrl = orderItem.getProductImageUrl();
        this.productCategoryName = orderItem.getProductCategoryName();
        this.price = orderItem.getPrice();
        this.quantity = orderItem.getQuantity();
    }
}
