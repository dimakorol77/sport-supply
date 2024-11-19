package org.example.services.interfaces;

import org.example.dto.PaymentRequestDto;
import org.example.dto.PaymentResponseDto;
import org.example.enums.PaymentStatus;
import org.example.models.Payment;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto, Long userId);
    PaymentResponseDto getPaymentStatus(Long paymentId, Long userId);
    void updatePaymentStatus(Long paymentId, PaymentStatus status);
    void updatePaymentStatusByOrder(Long orderId, PaymentStatus status);
}
