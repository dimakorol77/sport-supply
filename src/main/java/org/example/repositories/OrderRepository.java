package org.example.repositories;

import org.example.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Поиск заказов по ID пользователя
    List<Order> findByUserId(Long userId);
}