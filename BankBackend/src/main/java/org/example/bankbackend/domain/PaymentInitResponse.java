package org.example.bankbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bankbackend.domain.enums.PaymentStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInitResponse {
    private Long paymentId;
    private String paymentUrl;

    public PaymentInitResponse(){}

    public PaymentInitResponse(Long paymentId, String paymentUrl) {
        this.paymentId = paymentId;
        this.paymentUrl = paymentUrl;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}
