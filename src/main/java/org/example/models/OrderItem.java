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

    // Количество товара в заказе
    @Column(nullable = false)
    private Integer quantity;

    // Цена товара на момент заказа
    @Column(nullable = false)
    private BigDecimal price;

    // Цена со скидкой (если применимо)
    @Column(name = "discount_price")
    private BigDecimal discountPrice;

    // Связи

    // Заказ, к которому относится данный товар
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Продукт, который был заказан
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
