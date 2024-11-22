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
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    private LocalDateTime updatedAt;



    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)

    private List<CartItem> cartItems = new ArrayList<>();

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;


   // public void addToTotalPrice(BigDecimal amount) {
    //    this.totalPrice = this.totalPrice.add(amount);
   // }


   // public void subtractFromTotalPrice(BigDecimal amount) {
      //  this.totalPrice = this.totalPrice.subtract(amount);
   // }
}
