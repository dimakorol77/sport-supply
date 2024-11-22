package org.example.models;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Связь с избранными товарами
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

//    @PrePersist
//    public void prePersist() {
//        this.createdAt = LocalDateTime.now(); // Устанавливаем текущую дату и время при создании
//        this.updatedAt = LocalDateTime.now(); // Устанавливаем текущую дату и время при создании
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        this.updatedAt = LocalDateTime.now(); // Устанавливаем текущую дату и время при обновлении
//    }


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;


}

