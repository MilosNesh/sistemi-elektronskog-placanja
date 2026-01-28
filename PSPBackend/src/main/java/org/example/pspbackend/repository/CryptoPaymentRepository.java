package org.example.pspbackend.repository;

import org.example.pspbackend.domain.CryptoPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CryptoPaymentRepository extends JpaRepository<CryptoPayment, Long> {
    List<CryptoPayment> findByStatus(String status);
}
