package org.example.pspbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantRequest {
    private Long merchantId;
    private String merchantPassword;
    private double amount;
    private String currency;
    private String merchantOrderId;
    private Date merchantTimestamp;

    public String getMerchantPassword() {
        return merchantPassword;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public Date getMerchantTimestamp() {
        return merchantTimestamp;
    }
}
