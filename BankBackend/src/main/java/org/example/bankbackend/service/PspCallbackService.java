package org.example.bankbackend.service;

import org.example.bankbackend.domain.Payment;
import org.example.bankbackend.domain.paymentResponse.PaymentResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PspCallbackService {
    private final WebClient webClient;

    public PspCallbackService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void notifyPsp(Payment payment) {

        PaymentResponse response = new PaymentResponse(
                payment.getStatus(),
                payment.getGlobalTransactionId(),
                payment.getAcquirerTimestamp(),
                payment.getPspTimestamp(),
                payment.getStan(),
                payment.getMerchant().getId()
        );

        webClient.post()
                .uri("https://localhost:8445/payment/status")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(response)//"\"" + response + "\"")
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
