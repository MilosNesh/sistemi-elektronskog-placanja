package org.example.pspbackend.repository;

import org.example.pspbackend.domain.PaymentMethod;
import org.example.pspbackend.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    @Query("SELECT t FROM Transaction t  WHERE t.stan = :stan AND t.merchant.merchantId = :merchantId AND t.pspTimestamp = :pspTimestamp")
    Transaction find(@Param("stan") String stan, @Param("merchantId") Long merchantId, @Param("pspTimestamp") LocalDateTime pspTimestamp);

    @Query("SELECT t FROM Transaction t WHERE t.stan = :stan")
    Transaction findByStan(@Param("stan") String stan);
}
