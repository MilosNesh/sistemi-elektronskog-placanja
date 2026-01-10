package org.example.pspbackend.repository;

import org.example.pspbackend.domain.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    @Query("SELECT pm FROM PaymentMethod pm JOIN MerchantPaymentMethod mpm ON pm.paymentMethodId = mpm.paymentMethod.paymentMethodId WHERE mpm.merchant.merchantId = :merchantId AND mpm.isEnabled = true")
    List<PaymentMethod> findByMerchantId(@Param("merchantId") Long merchantId);
}
