package org.example.pspbackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.pspbackend.dto.CurrencyFreaksResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class CryptoRateService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${currencyfreaks.api.key}")
    private String apiKey;

    public BigDecimal convertRsdToEth(BigDecimal rsdAmount) {

        BigDecimal btcUsdRate = getEthUsdRate();
        BigDecimal usdRsdRate = getUsdRsdRate();

        // BTC = RSD / (BTC_USD * USD_RSD)
        BigDecimal denominator = btcUsdRate.multiply(usdRsdRate);

        return rsdAmount.divide(
                denominator,
                8,
                RoundingMode.HALF_UP
        );
    }

    private BigDecimal getEthUsdRate() {
        String url =
                "https://api.coingecko.com/api/v3/simple/price" +
                        "?ids=ethereum&vs_currencies=usd";

        JsonNode root = restTemplate.getForObject(url, JsonNode.class);

        if (root == null ||
                !root.has("ethereum") ||
                !root.get("ethereum").has("usd")) {
            throw new IllegalStateException("Invalid CoinGecko response");
        }

        return root
                .get("ethereum")
                .get("usd")
                .decimalValue(); // uvek BigDecimal
    }

    private BigDecimal getUsdRsdRate() {
        String url =
                "https://api.currencyfreaks.com/v2.0/rates/latest" +
                        "?apikey=" + apiKey +
                        "&symbols=RSD";

        CurrencyFreaksResponse response =
                restTemplate.getForObject(url, CurrencyFreaksResponse.class);

        if (response == null ||
                response.getRates() == null ||
                !response.getRates().containsKey("RSD")) {
            throw new IllegalStateException("Invalid CurrencyFreaks response");
        }

        return response.getRates().get("RSD");
    }
}
