package org.example.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Путь к файлу изображения
    @Column(name = "url", nullable = false)
    private String url;

    // Альтернативный текст для изображения
    @Column(name = "alt_text")
    private String altText;

    // Дата создания и обновления
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Связь с продуктом
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
