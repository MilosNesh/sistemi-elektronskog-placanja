package org.example.pspbackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BlockstreamClient {
    private final WebClient webClient;

    public BlockstreamClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://blockstream.info/testnet/api").build();
    }

    public JsonNode getAddressInfo(String address){
        return webClient.get()
                .uri("address/{address}",address)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }
}
