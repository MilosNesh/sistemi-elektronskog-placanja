package org.example.pspbackend.service.impl;

import org.example.pspbackend.dto.PaymentInitRequest;
import org.example.pspbackend.dto.PaymentInitResponse;
import org.example.pspbackend.service.CallMerchantApiService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CallMerchantApiServiceImpl implements CallMerchantApiService {
    private final WebClient webClient = WebClient.create();

    public void notifyPaymentSuccess(String successUrl) {
        webClient.post()
                .uri(successUrl)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void notifyPaymentFailed(String failedUrl) {
        webClient.post()
                .uri(failedUrl)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void notifyPaymentError(String errorUrl) {
        webClient.post()
                .uri(errorUrl)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
