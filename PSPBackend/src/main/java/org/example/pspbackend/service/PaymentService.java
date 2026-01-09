package org.example.pspbackend.service;

import org.example.pspbackend.dto.MerchantRequest;
import org.example.pspbackend.dto.PaymentMethodDTO;

public interface PaymentService {
    public String generatePaymentUrl(MerchantRequest merchantRequest);
    public String executePayment(String transactionId, PaymentMethodDTO request);
}
