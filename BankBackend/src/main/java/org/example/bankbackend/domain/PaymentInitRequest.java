package org.example.bankbackend.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInitRequest {
    private Long merchantId;
    private Double amount;
    private String currency;
    private String stan;
    private String timestamp;
}
