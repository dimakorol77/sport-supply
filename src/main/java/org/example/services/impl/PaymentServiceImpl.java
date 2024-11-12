package org.example.services.impl;

import org.example.dto.PaymentDto;
import org.example.exception.OrderNotFoundException;
import org.example.exception.PaymentNotFoundException;
import org.example.exception.errorMessage.ErrorMessage;
import org.example.mappers.PaymentMapper;
import org.example.models.Order;
import org.example.models.Payment;
import org.example.repositories.OrderRepository;
import org.example.repositories.PaymentRepository;
import org.example.services.interfaces.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public PaymentDto createPayment(PaymentDto paymentDto) {
        // Находим заказ по ID
        Order order = orderRepository.findById(paymentDto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(ErrorMessage.ORDER_NOT_FOUND));

        // Создаем сущность Payment из DTO
        Payment payment = paymentMapper.toEntity(paymentDto, order);

        // Сохраняем оплату в базе данных
        Payment savedPayment = paymentRepository.save(payment);

        // Возвращаем DTO с сохраненной оплатой
        return paymentMapper.toDto(savedPayment);
    }

    // Получение статуса платежа по ID
    @Override
    public PaymentDto getPaymentStatus(Long paymentId) {
        // Находим платеж по ID
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(ErrorMessage.PAYMENT_NOT_FOUND));

        // Возвращаем DTO с информацией о платеже
        return paymentMapper.toDto(payment);
    }
}
