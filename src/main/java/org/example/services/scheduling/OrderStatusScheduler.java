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
    //для автоматического обновления статусов заказов каждые 30 секунд
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Autowired
    public OrderStatusScheduler(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    // Аннотация @Scheduled указывает, что метод должен выполняться периодически
    @Scheduled(fixedRate = 30000)  // Задаем интервал выполнения задачи в миллисекундах (30000 мс = 30 секунд)
    public void updateOrderStatuses() {
        // Получаем список статусов заказов, которые необходимо обновить
        List<OrderStatus> statusesToUpdate = Arrays.asList(
                OrderStatus.PAID,
                OrderStatus.PROCESSING,
                OrderStatus.SHIPPED
        );

        // Находим все заказы с указанными статусами
        List<Order> ordersToUpdate = orderRepository.findByStatusIn(statusesToUpdate);

        // Проходим по каждому заказу и обновляем его статус
        for (Order order : ordersToUpdate) {
            OrderStatus currentStatus = order.getStatus();

            // Определяем следующий статус для заказа
            OrderStatus nextStatus = getNextStatus(currentStatus);

            if (nextStatus != null) {
                // Обновляем статус заказа через сервис, чтобы сохранить историю изменений
                orderService.updateOrderStatus(order.getId(), nextStatus);
            }
        }
    }

    // Метод для определения следующего статуса заказа
    private OrderStatus getNextStatus(OrderStatus currentStatus) {
        switch (currentStatus) {
            case PAID:
                return OrderStatus.PROCESSING;  // Из статуса PAID переходим в PROCESSING
            case PROCESSING:
                return OrderStatus.SHIPPED;     // Из статуса PROCESSING переходим в SHIPPED
            case SHIPPED:
                return OrderStatus.DELIVERED;   // Из статуса SHIPPED переходим в DELIVERED
            default:
                return null;  // Если статус не подлежит обновлению, возвращаем null
        }
    }
}
