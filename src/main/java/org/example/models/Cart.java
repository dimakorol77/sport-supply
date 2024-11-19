package org.example.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Используем ID пользователя как ID корзины


    // Дата и время создания корзины
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Дата и время обновления корзины
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // Пользователь, которому принадлежит корзина
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    // Товары в корзине
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    //private List<CartItem> cartItems;
    private List<CartItem> cartItems = new ArrayList<>();

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO; // Добавляем поле общей стоимости

    // Метод для увеличения общей стоимости
    public void addToTotalPrice(BigDecimal amount) {
        this.totalPrice = this.totalPrice.add(amount);
    }

    // Метод для уменьшения общей стоимости
    public void subtractFromTotalPrice(BigDecimal amount) {
        this.totalPrice = this.totalPrice.subtract(amount);
    }
}
