package org.example.bankbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

//@Getter
//@AllArgsConstructor
public class PaymentFormResponse {
    private Long paymentId;
    private Double amount;
    private String currency;
    private Boolean expired;

    public PaymentFormResponse() {}

    public PaymentFormResponse(Long paymentId, Double amount, String currency, Boolean expired) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.currency = currency;
        this.expired = expired;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Boolean getExpired() {
        return expired;
    }
}
