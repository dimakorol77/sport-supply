package org.example.repositories;

import org.example.enums.PaymentStatus;
import org.example.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

   // List<Payment> findByOrderId(Long orderId);
    List<Payment> findByStatus(PaymentStatus status);
    Optional<Payment> findByOrderId(Long orderId);
}
