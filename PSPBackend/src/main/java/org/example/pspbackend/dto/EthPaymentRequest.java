package org.example.pspbackend.dto;

import java.math.BigDecimal;

public class EthPaymentRequest {
    private String toAddress;
    private BigDecimal amount;

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
