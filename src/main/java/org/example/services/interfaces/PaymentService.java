package org.example.services.interfaces;

import org.example.dto.PaymentDto;

public interface PaymentService {
    PaymentDto createPayment(PaymentDto paymentDto);
    PaymentDto getPaymentStatus(Long paymentId);
}
