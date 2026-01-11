package org.example.bankbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.bankbackend.domain.enums.PaymentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PaymentResponse {
    private PaymentStatus status;
    private String globalTransactionId;
    private LocalDateTime acquirerTimestamp;

    public PaymentResponse(PaymentStatus status, String globalTransactionId, LocalDateTime acquirerTimestamp) {
        this.status = status;
        this.globalTransactionId = globalTransactionId;
        this.acquirerTimestamp = acquirerTimestamp;
    }
}
