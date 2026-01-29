package org.example.pspbackend.dto;

import java.math.BigDecimal;
import java.util.Map;

public class CurrencyFreaksResponse {
    private String date;
    private String base;
    private Map<String, BigDecimal> rates;

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}
