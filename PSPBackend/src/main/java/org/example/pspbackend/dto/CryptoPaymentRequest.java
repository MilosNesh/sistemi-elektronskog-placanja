package org.example.pspbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CryptoPaymentRequest {
    private BigDecimal amount;
}
