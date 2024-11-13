package org.example.controllers;

import org.example.annotations.CreatePayment;
import org.example.annotations.GetPaymentStatus;
import org.example.dto.PaymentDto;
import org.example.services.interfaces.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Создание нового платежа
    @CreatePayment
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto paymentDto) {
        // Вызов сервиса для создания платежа
        PaymentDto createdPayment = paymentService.createPayment(paymentDto);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    // Получение статуса платежа
    @GetPaymentStatus
    public ResponseEntity<PaymentDto> getPaymentStatus(@PathVariable Long paymentId) {
        // Вызов сервиса для получения статуса платежа
        PaymentDto paymentDto = paymentService.getPaymentStatus(paymentId);
        return new ResponseEntity<>(paymentDto, HttpStatus.OK);
    }
}
