package org.example.repositories;

import org.example.models.Cart;
import org.example.models.CartItem;
import org.example.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

   Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

}
