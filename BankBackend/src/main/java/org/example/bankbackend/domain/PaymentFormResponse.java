package org.example.bankbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentFormResponse {
    private Long paymentId;
    private Double amount;
    private String currency;
    private Boolean expired;

    public PaymentFormResponse(Long paymentId, Double amount, String currency, Boolean expired) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.currency = currency;
        this.expired = expired;
    }
}
