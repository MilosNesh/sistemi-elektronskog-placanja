package org.example.bankbackend.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardPaymentRequest {
    private String pan;
    private String securityCode;
    private String cardHolderName;
    private String expiryDate; // MM/YY
}
