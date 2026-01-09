package org.example.bankbackend.domain.paymentResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QrPaymentResponse {
    private Long paymentId;
    private String qrPayload;
}
