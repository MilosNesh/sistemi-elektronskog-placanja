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
}
