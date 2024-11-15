package org.example.models;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.Form;
import org.example.enums.ProteinType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    private String description;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "protein_type")
    private ProteinType proteinType;

    @Column(name = "vitamin_group")
    private String vitaminGroup;

    @Enumerated(EnumType.STRING)
    private Form form;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Image> images;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product")
    private List<Discount> discounts;

    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();


}