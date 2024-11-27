package org.example.repositories;

import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;
import org.example.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findByStatus(OrderStatus status);
    List<Order> findByCreatedAtAfter(LocalDateTime date);
    List<Order> findByDeliveryMethod(DeliveryMethod deliveryMethod);
    List<Order> findByStatusIn(List<OrderStatus> statuses);
}