package org.example.repositories;

import org.example.models.Cart;
import org.example.models.CartItem;
import org.example.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

   Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

}
