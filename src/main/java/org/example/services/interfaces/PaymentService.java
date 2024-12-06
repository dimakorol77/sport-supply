package org.example.services.interfaces;

import org.example.dto.PaymentRequestDto;
import org.example.dto.PaymentResponseDto;
import org.example.enums.PaymentStatus;
import org.example.models.Payment;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto);
    PaymentResponseDto getPaymentStatus(Long paymentId);
    void updatePaymentStatus(Long paymentId, PaymentStatus status);

}
