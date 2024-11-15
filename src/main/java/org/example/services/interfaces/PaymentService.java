package org.example.services.interfaces;

import org.example.dto.PaymentRequestDto;
import org.example.dto.PaymentResponseDto;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto);
    PaymentResponseDto getPaymentStatus(Long paymentId);
}
