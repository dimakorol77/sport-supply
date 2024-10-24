package org.example.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    private Long id; // Используем ID пользователя как ID корзины

    // Дата и время создания корзины
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Дата и время обновления корзины
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Связи

    // Пользователь, которому принадлежит корзина
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    // Товары в корзине
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;
}
