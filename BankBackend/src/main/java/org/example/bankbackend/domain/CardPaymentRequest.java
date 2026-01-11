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

    public String getPan() {
        return pan;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
