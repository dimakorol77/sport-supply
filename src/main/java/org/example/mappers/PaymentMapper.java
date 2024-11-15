package org.example.mappers;

import org.example.dto.PaymentRequestDto;
import org.example.dto.PaymentResponseDto;
import org.example.enums.PaymentStatus;
import org.example.models.Order;
import org.example.models.Payment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class PaymentMapper {
    // Преобразование сущности Payment в PaymentResponseDto
    public PaymentResponseDto toResponseDto(Payment payment) {
        if (payment == null) {
            return null;
        }
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrder().getId());
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getStatus());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());
        return dto;
    }

    // Преобразование PaymentRequestDto в сущность Payment
    public Payment toEntity(PaymentRequestDto dto, Order order) {
        if (dto == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(dto.getAmount());
        payment.setStatus(PaymentStatus.PENDING); // Устанавливаем статус по умолчанию
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        return payment;
    }

//    public void updateEntityFromDto(PaymentRequestDto dto, Payment payment) {
//        if (dto == null || payment == null) {
//            return;
//        }
//        payment.setAmount(dto.getAmount());
//        payment.setStatus(dto.getStatus());
//        payment.setUpdatedAt(LocalDateTime.now()); // Время обновления
//    }
}
