package org.example.pspbackend.service;

import org.example.pspbackend.dto.PaymentMethodDTO;

public interface PaymentProvider {
    String executePayment(String transactionId, PaymentMethodDTO request);
}
