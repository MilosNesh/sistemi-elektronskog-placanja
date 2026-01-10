package org.example.pspbackend.service.impl;

import org.example.pspbackend.dto.PaymentMethodDTO;
import org.example.pspbackend.service.PaymentProvider;
import org.springframework.stereotype.Service;

@Service
public class QrPaymentProvider implements PaymentProvider {

    @Override
    public String executePayment(String transactionId, PaymentMethodDTO request) {
        // logika za QR payment
        return "redirect-url-for-qr";
    }
}
