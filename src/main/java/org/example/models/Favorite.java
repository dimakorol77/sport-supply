package org.example.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связь с пользователем
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Связь с продуктом
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Дата добавления в избранное
    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;
}
