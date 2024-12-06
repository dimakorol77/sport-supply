package org.example.services.scheduling;

import org.example.enums.OrderStatus;
import org.example.models.Order;
import org.example.repositories.OrderRepository;
import org.example.services.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderStatusScheduler {
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Autowired
    public OrderStatusScheduler(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @Scheduled(fixedRate = 30000)
    public void updateOrderStatuses() {
        List<OrderStatus> statusesToUpdate = Arrays.asList(
                OrderStatus.PAID,
                OrderStatus.PROCESSING,
                OrderStatus.SHIPPED
        );

        List<Order> ordersToUpdate = orderRepository.findByStatusIn(statusesToUpdate);

        for (Order order : ordersToUpdate) {
            OrderStatus currentStatus = order.getStatus();
            OrderStatus nextStatus = getNextStatus(currentStatus);

            if (nextStatus != null) {
                orderService.updateOrderStatusBySystem(order.getId(), nextStatus);
            }
        }
    }

    private OrderStatus getNextStatus(OrderStatus currentStatus) {
        switch (currentStatus) {
            case PAID:
                return OrderStatus.PROCESSING;
            case PROCESSING:
                return OrderStatus.SHIPPED;
            case SHIPPED:
                return OrderStatus.DELIVERED;
            default:
                return null;
        }
    }
}
