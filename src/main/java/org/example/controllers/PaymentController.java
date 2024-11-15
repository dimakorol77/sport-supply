package org.example.controllers;


import jakarta.validation.Valid;
import org.example.annotation.payment.CreatePayment;
import org.example.annotation.payment.GetPaymentStatus;
import org.example.dto.PaymentRequestDto;
import org.example.dto.PaymentResponseDto;

import org.example.services.interfaces.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@Validated
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Создание нового платежа
    @CreatePayment
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody @Valid PaymentRequestDto paymentRequestDto) {
        PaymentResponseDto createdPayment = paymentService.createPayment(paymentRequestDto);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    // Получение статуса платежа
    @GetPaymentStatus
    public ResponseEntity<PaymentResponseDto> getPaymentStatus(@PathVariable Long paymentId) {
        // Вызов сервиса для получения статуса платежа
        PaymentResponseDto paymentResponseDto = paymentService.getPaymentStatus(paymentId);
        return new ResponseEntity<>(paymentResponseDto, HttpStatus.OK);
    }
}
