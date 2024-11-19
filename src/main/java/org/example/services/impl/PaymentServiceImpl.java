package org.example.services.impl;

import org.example.dto.PaymentRequestDto;
import org.example.dto.PaymentResponseDto;
import org.example.exceptions.OrderNotFoundException;
import org.example.exceptions.PaymentAlreadyExistsException;
import org.example.exceptions.PaymentNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.PaymentMapper;
import org.example.models.Order;
import org.example.models.Payment;
import org.example.repositories.OrderRepository;
import org.example.repositories.PaymentRepository;
import org.example.services.interfaces.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              OrderRepository orderRepository,
                              PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto) {
        // Находим заказ по ID
        Order order = orderRepository.findById(paymentRequestDto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(ErrorMessage.ORDER_NOT_FOUND));

        // Проверяем, существует ли платеж для этого заказа
        Optional<Payment> existingPayment = paymentRepository.findByOrderId(order.getId());
        if (existingPayment.isPresent()) {
            throw new PaymentAlreadyExistsException(ErrorMessage.PAYMENT_ALREADY_EXISTS);
        }

        // Создаем сущность Payment из PaymentRequestDto
        Payment payment = paymentMapper.toEntity(paymentRequestDto, order);

        // Сохраняем платеж в базе данных
        Payment savedPayment = paymentRepository.save(payment);

        // Возвращаем PaymentResponseDto с сохраненной информацией
        return paymentMapper.toResponseDto(savedPayment);
    }

    // Получение статуса платежа по ID
    @Override
    public PaymentResponseDto getPaymentStatus(Long paymentId) {
        // Находим платеж по ID
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(ErrorMessage.PAYMENT_NOT_FOUND));

        // Возвращаем DTO с информацией о платеже
        return paymentMapper.toResponseDto(payment);
    }
}
