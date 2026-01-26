package org.example.bankbackend.domain.paymentResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.bankbackend.domain.enums.PaymentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
//@AllArgsConstructor
public class PaymentResponse {
    private PaymentStatus status;
    private String globalTransactionId;
    private LocalDateTime acquirerTimestamp;
    private String pspTimestamp;
    private String stan;
    private Long merchantId;

    public PaymentResponse(PaymentStatus paymentStatus, String globalTransactionId, LocalDateTime acquirerTimestamp, String pspTimestamp, String stan, Long merchantId) {
        this.status = paymentStatus;
        this.globalTransactionId = globalTransactionId;
        this.pspTimestamp = pspTimestamp;
        this.stan = stan;
        this.merchantId = merchantId;
        this.acquirerTimestamp = acquirerTimestamp;
    }
    public PaymentStatus getStatus() {
        return status;
    }

    public String getGlobalTransactionId() {
        return globalTransactionId;
    }

    public LocalDateTime getAcquirerTimestamp() {
        return acquirerTimestamp;
    }

    public String getPspTimestamp() {
        return pspTimestamp;
    }

    public String getStan() {
        return stan;
    }

    public Long getMerchantId() {
        return merchantId;
    }
}
