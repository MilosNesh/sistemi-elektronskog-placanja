package org.example.webshopbackend.service.impl;

import org.example.webshopbackend.dto.MerchantRequest;
import org.example.webshopbackend.service.CallApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CallApiServiceImpl implements CallApiService {

    private final WebClient webClient = WebClient.create();

    @Value("${psp.url}")
    private String url;

    public String callApi(MerchantRequest merchantRequest) {
        return webClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(merchantRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
