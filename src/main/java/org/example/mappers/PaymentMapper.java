package org.example.mappers;

import org.example.dto.PaymentDto;
import org.example.enums.PaymentStatus;
import org.example.models.Order;
import org.example.models.Payment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class PaymentMapper {
    // Преобразование сущности Payment в DTO
    public PaymentDto toDto(Payment payment) {
        if (payment == null) {
            return null;
        }
        PaymentDto dto = new PaymentDto();
        dto.setOrderId(payment.getOrder().getId()); // Ссылаемся на заказ
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getStatus());
        return dto;
    }

    // Преобразование DTO в сущность Payment
    public Payment toEntity(PaymentDto dto, Order order) {
        if (dto == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setOrder(order); // Связываем с заказом
        payment.setAmount(dto.getAmount());
        payment.setStatus(PaymentStatus.PENDING); // Статус по умолчанию
        payment.setCreatedAt(LocalDateTime.now()); // Время создания
        return payment;
    }

    // Обновление сущности Payment из DTO
    public void updateEntityFromDto(PaymentDto dto, Payment payment) {
        if (dto == null || payment == null) {
            return;
        }
        payment.setAmount(dto.getAmount());
        payment.setStatus(dto.getStatus());
        payment.setUpdatedAt(LocalDateTime.now()); // Время обновления
    }
}
