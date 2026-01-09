package org.example.pspbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInitRequest {
    private Long merchantId;
    private Double amount;
    private String currency;
    private String stan;
    private String pspTimestamp;

    public PaymentInitRequest() {}

    public PaymentInitRequest(Long merchantId, Double amount, String currency, String stan, String pspTimestamp) {
        this.merchantId = merchantId;
        this.amount = amount;
        this.currency = currency;
        this.stan = stan;
        this.pspTimestamp = pspTimestamp;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getStan() {
        return stan;
    }

    public String getPspTimestamp() {
        return pspTimestamp;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public void setPspTimestamp(String pspTimestamp) {
        this.pspTimestamp = pspTimestamp;
    }
}
