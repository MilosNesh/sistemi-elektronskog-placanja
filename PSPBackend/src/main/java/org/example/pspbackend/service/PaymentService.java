package org.example.pspbackend.service;

import org.example.pspbackend.dto.MerchantRequest;
import org.example.pspbackend.dto.PaymentMethodDTO;
import org.example.pspbackend.dto.PaymentResponse;

public interface PaymentService {
    public String generatePaymentUrl(MerchantRequest merchantRequest);
    public String executePayment(String transactionId, PaymentMethodDTO request);
    public void sendPaymentStatusToMerchant(PaymentResponse paymentResponse);
}
