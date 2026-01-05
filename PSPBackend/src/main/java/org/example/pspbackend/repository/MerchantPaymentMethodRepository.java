package org.example.pspbackend.repository;

import org.example.pspbackend.domain.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantPaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

}
