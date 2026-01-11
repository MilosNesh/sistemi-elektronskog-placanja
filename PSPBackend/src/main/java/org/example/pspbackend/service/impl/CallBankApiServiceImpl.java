package org.example.pspbackend.service.impl;

import org.example.pspbackend.dto.PaymentInitRequest;
import org.example.pspbackend.dto.PaymentInitResponse;
import org.example.pspbackend.service.CallBankApiService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CallBankApiServiceImpl implements CallBankApiService {

    private final WebClient webClient = WebClient.create();

    public PaymentInitResponse createPaymentInitRequest(PaymentInitRequest paymentInitRequest, String signature){
        return webClient
                .post()
                .uri("https://localhost:8443/init")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-PSP-SIGNATURE", signature)
                .bodyValue(paymentInitRequest)
                .retrieve()
                .bodyToMono(PaymentInitResponse.class)
                .block();
    }
}
