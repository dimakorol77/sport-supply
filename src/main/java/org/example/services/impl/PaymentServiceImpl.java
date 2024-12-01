package org.example.services.impl;

import org.example.dto.PaymentRequestDto;
import org.example.dto.PaymentResponseDto;
import org.example.enums.OrderStatus;
import org.example.enums.PaymentStatus;
import org.example.enums.Role;
import org.example.exceptions.OrderNotFoundException;
import org.example.exceptions.PaymentAlreadyExistsException;
import org.example.exceptions.PaymentNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.PaymentMapper;
import org.example.models.Order;
import org.example.models.OrderStatusHistory;
import org.example.models.Payment;
import org.example.models.User;
import org.example.repositories.OrderRepository;
import org.example.repositories.OrderStatusHistoryRepository;
import org.example.repositories.PaymentRepository;
import org.example.security.SecurityUtils;
import org.example.services.interfaces.PaymentService;
import org.example.services.interfaces.UserService;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;
    private final SecurityUtils securityUtils;

    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              OrderRepository orderRepository,
                              PaymentMapper paymentMapper,
                              OrderStatusHistoryRepository orderStatusHistoryRepository,
                              SecurityUtils securityUtils) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentMapper = paymentMapper;
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
        this.securityUtils = securityUtils;
    }

    private User getCurrentUser() {
        return securityUtils.getCurrentUser();
    }
    private void checkOrderOwnership(Order order, User user) {
        if (!order.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }
    }

    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto, Long userId) {
        Order order = orderRepository.findById(paymentRequestDto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(ErrorMessage.ORDER_NOT_FOUND));

        User currentUser = getCurrentUser();
        checkOrderOwnership(order, currentUser);

        Optional<Payment> existingPayment = paymentRepository.findByOrderId(order.getId());
        if (existingPayment.isPresent()) {
            throw new PaymentAlreadyExistsException(ErrorMessage.PAYMENT_ALREADY_EXISTS);
        }

        Payment payment = paymentMapper.toEntity(paymentRequestDto, order);

        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponseDto(savedPayment);
    }

    @Override
    public PaymentResponseDto getPaymentStatus(Long paymentId, Long userId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(ErrorMessage.PAYMENT_NOT_FOUND));

        User currentUser = getCurrentUser();
        checkOrderOwnership(payment.getOrder(), currentUser);

        return paymentMapper.toResponseDto(payment);
    }

    @Override
    public void updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(ErrorMessage.PAYMENT_NOT_FOUND));

        payment.setStatus(status);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        updateOrderStatusBasedOnPayment(payment);
    }

    @Override
    public void updatePaymentStatusByOrder(Long orderId, PaymentStatus status) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException(ErrorMessage.PAYMENT_NOT_FOUND));

        payment.setStatus(status);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        updateOrderStatusBasedOnPayment(payment);
    }

    private void updateOrderStatusBasedOnPayment(Payment payment) {
        Order order = payment.getOrder();

        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            order.setStatus(OrderStatus.PAID);
        } else if (payment.getStatus() == PaymentStatus.FAILED) {
            order.setStatus(OrderStatus.CANCELLED);
        }

        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        addOrderStatusHistory(order, order.getStatus());
    }
    private void addOrderStatusHistory(Order order, OrderStatus status) {
        OrderStatusHistory statusHistory = new OrderStatusHistory();
        statusHistory.setOrder(order);
        statusHistory.setStatus(status);
        statusHistory.setChangedAt(LocalDateTime.now());

        if (order.getStatusHistory() == null) {
            order.setStatusHistory(new ArrayList<>());
        }
        order.getStatusHistory().add(statusHistory);
        orderStatusHistoryRepository.save(statusHistory);
    }
}
