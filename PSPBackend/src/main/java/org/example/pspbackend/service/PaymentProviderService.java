package org.example.pspbackend.service;

import org.example.pspbackend.dto.PaymentMethodDTO;

public interface PaymentProviderService {
    String processPayment(String transactionId, PaymentMethodDTO request);
    String getSupportedPaymentType();
}
