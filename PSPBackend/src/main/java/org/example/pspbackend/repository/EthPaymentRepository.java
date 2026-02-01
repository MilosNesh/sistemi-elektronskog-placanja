package org.example.pspbackend.repository;

import org.example.pspbackend.domain.ETHPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EthPaymentRepository extends JpaRepository<ETHPayment, Long> {
    ETHPayment findByPaymentId(String paymentId);
}
