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
import org.example.services.interfaces.UserService;
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
    private final UserService userService;

    @Autowired
    public PaymentController(PaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }

    @CreatePayment
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody @Valid PaymentRequestDto paymentRequestDto) {
        String email = SecurityUtils.getCurrentUserEmail();
        User user = userService.getUserByEmail(email);
        PaymentResponseDto createdPayment = paymentService.createPayment(paymentRequestDto, user.getId());
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    @GetPaymentStatus
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponseDto> getPaymentStatus(@PathVariable Long paymentId) {
        String email = SecurityUtils.getCurrentUserEmail();
        User user = userService.getUserByEmail(email);
        PaymentResponseDto paymentResponseDto = paymentService.getPaymentStatus(paymentId, user.getId());
        return new ResponseEntity<>(paymentResponseDto, HttpStatus.OK);
    }

    @UpdatePaymentStatus
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updatePaymentStatus(@PathVariable Long paymentId, @RequestParam PaymentStatus status) {
        paymentService.updatePaymentStatus(paymentId, status);
        return ResponseEntity.ok().build();
    }
}
