package org.example.pspbackend.service.impl;

import org.example.pspbackend.dto.PaymentInitRequest;
import org.example.pspbackend.dto.PaymentInitResponse;
import org.example.pspbackend.service.CallMerchantApiService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class CallMerchantApiServiceImpl implements CallMerchantApiService {
    private final WebClient webClient = WebClient.create();

    public void notifyPaymentSuccess(String successUrl, String merchantOrderId) {
        webClient.post()
                .uri(successUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue("\"" + merchantOrderId + "\"") // dodaj navodnike
                .retrieve()
                .toBodilessEntity()
                .block();
    }


    public void notifyPaymentFailed(String failedUrl, String merchantOrderId) {
        webClient.post()
                .uri(failedUrl)
                .bodyValue(merchantOrderId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void notifyPaymentError(String errorUrl, String merchantOrderId) {
        webClient.post()
                .uri(errorUrl)
                .bodyValue(merchantOrderId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
