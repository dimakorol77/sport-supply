package org.example.repositories;

import org.example.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Поиск товаров по ID заказа
    List<OrderItem> findByOrderId(Long orderId);


}