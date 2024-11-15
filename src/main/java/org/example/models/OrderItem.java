package org.example.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    // Информация о продукте на момент покупки
    private Long productId;
    private String productName;
    private String productDescription;
    private String productImageUrl;
    private String productCategoryName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal price;

    // Связь с заказом
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;


}
