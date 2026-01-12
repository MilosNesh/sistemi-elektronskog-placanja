package org.example.pspbackend.service;

import org.example.pspbackend.dto.PaymentInitRequest;
import org.example.pspbackend.dto.PaymentInitResponse;

public interface CallBankApiService {
    public PaymentInitResponse createPaymentInitRequest(PaymentInitRequest paymentInitRequest, String signature, String type);
}
