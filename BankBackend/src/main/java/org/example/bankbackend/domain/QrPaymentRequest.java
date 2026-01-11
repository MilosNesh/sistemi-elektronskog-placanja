package org.example.bankbackend.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QrPaymentRequest {
    private String qrPayload;

    public String getQrPayload() {
        return qrPayload;
    }
}
