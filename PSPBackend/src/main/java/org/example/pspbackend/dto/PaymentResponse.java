package org.example.pspbackend.dto;

import java.time.LocalDateTime;

public class PaymentResponse {
    private String status;
    private String globalTransactionId;
    private LocalDateTime acquirerTimestamp;
    private String stan;
    private Long merchantId;
    private String pspTimestamp;

    public PaymentResponse() {}

    public PaymentResponse(String status, String globalTransactionId, LocalDateTime acquirerTimestamp, String stan) {
        this.status = status;
        this.globalTransactionId = globalTransactionId;
        this.acquirerTimestamp = acquirerTimestamp;
        this.stan = stan;
    }

    public String getStatus() {
        return status;
    }

    public String getGlobalTransactionId() {
        return globalTransactionId;
    }

    public LocalDateTime getAcquirerTimestamp() {
        return acquirerTimestamp;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGlobalTransactionId(String globalTransactionId) {
        this.globalTransactionId = globalTransactionId;
    }

    public void setAcquirerTimestamp(LocalDateTime acquirerTimestamp) {
        this.acquirerTimestamp = acquirerTimestamp;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }
    public String getStan() {
        return stan;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public String getPspTimestamp() {
        return pspTimestamp;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public void setPspTimestamp(String pspTimestamp) {
        this.pspTimestamp = pspTimestamp;
    }
}
