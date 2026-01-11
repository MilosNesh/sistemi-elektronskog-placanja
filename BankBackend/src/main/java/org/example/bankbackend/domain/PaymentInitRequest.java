package org.example.bankbackend.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({ "merchantId", "amount", "currency", "stan", "pspTimestamp" })
public class PaymentInitRequest {
    private Long merchantId;
    private Double amount;
    private String currency;
    private String stan;
    private String pspTimestamp;

    public Long getMerchantId() {
        return merchantId;
    }

    public Double getAmount() {
        return amount;
    }

    public String getStan() {
        return stan;
    }

    public String getCurrency() {
        return currency;
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
