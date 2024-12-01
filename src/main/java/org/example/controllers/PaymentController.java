package org.example.controllers;


import jakarta.validation.Valid;
import org.example.annotations.PaymentAnnotations.CreatePayment;
import org.example.annotations.PaymentAnnotations.GetPaymentStatus;
import org.example.annotations.PaymentAnnotations.UpdatePaymentStatus;
import org.example.dto.PaymentRequestDto;
import org.example.dto.PaymentResponseDto;

import org.example.enums.PaymentStatus;
import org.example.models.User;
import org.example.security.SecurityUtils;
import org.example.services.interfaces.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@Validated
public class PaymentController {
    private final PaymentService paymentService;
    private final SecurityUtils securityUtils;

    @Autowired
    public PaymentController(PaymentService paymentService, SecurityUtils securityUtils) {
        this.paymentService = paymentService;
        this.securityUtils = securityUtils;
    }
    private User getCurrentUser() {
        return securityUtils.getCurrentUser();
    }
    @CreatePayment
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody @Valid PaymentRequestDto paymentRequestDto) {
        User currentUser = getCurrentUser();
        PaymentResponseDto createdPayment = paymentService.createPayment(paymentRequestDto, currentUser.getId());
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    @GetPaymentStatus
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponseDto> getPaymentStatus(@PathVariable Long paymentId) {
        User currentUser = getCurrentUser();
        PaymentResponseDto paymentResponseDto = paymentService.getPaymentStatus(paymentId, currentUser.getId());
        return new ResponseEntity<>(paymentResponseDto, HttpStatus.OK);
    }

    @UpdatePaymentStatus
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updatePaymentStatus(@PathVariable Long paymentId, @RequestParam PaymentStatus status) {
        paymentService.updatePaymentStatus(paymentId, status);
        return ResponseEntity.ok().build();
    }
}
