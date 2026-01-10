package org.example.pspbackend.service.impl;

import org.example.pspbackend.dto.PaymentMethodDTO;
import org.example.pspbackend.service.PaymentProviderService;
import org.springframework.stereotype.Service;

@Service
public class QrPaymentProvider implements PaymentProviderService {

    @Override
    public String getSupportedPaymentType() {
        return "QR_CODE";
    }

    @Override
    public String processPayment(String transactionId, PaymentMethodDTO request) {
        // logika za QR payment
        return "redirect-url-for-qr";
    }
}
