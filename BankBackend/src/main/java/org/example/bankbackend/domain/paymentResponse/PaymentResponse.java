package org.example.bankbackend.domain.paymentResponse;

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
    private String pspTimestamp;
    private String stan;
    private Long merchantId;
}
