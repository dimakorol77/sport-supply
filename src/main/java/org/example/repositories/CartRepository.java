package org.example.repositories;

import org.example.models.Cart;
import org.example.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {


   // Optional<Cart> findById(Long id);
    boolean existsByUserId(Long userId);
    Optional<Cart> findByUserId(Long userId);
}