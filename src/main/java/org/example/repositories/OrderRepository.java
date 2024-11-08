package org.example.repositories;

import org.example.enums.OrderStatus;
import org.example.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Поиск заказов по ID пользователя
    List<Order> findByUserId(Long userId);
    //дописать поиск по статусу заказа, дате создания и методу доставки,

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByCreatedAtAfter(LocalDateTime date);

    List<Order> findByDeliveryMethod(String deliveryMethod);
}