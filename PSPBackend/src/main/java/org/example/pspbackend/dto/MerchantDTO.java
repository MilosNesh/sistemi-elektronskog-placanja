package org.example.pspbackend.dto;

import lombok.*;
import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.domain.PaymentMethod;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDTO {
    private Long merchantId;
    private String merchantUsername;
    private String sellerUrl;
    private Integer port;
    private String successUrl;
    private String failedUrl;
    private String errorUrl;
    private List<PaymentMethod> paymentMethods;

    public MerchantDTO(Merchant merchant) {
        this.merchantId = merchant.getMerchantId();
        this.merchantUsername = merchant.getMerchantUsername();
        this.sellerUrl = merchant.getSellerUrl();
        this.port = merchant.getPort();
        this.successUrl = merchant.getSuccessUrl();
        this.failedUrl = merchant.getFailedUrl();
        this.errorUrl = merchant.getErrorUrl();
        this.paymentMethods = merchant.getPaymentMethods();
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public String getMerchantUsername() {
        return merchantUsername;
    }

    public String getSellerUrl() {
        return sellerUrl;
    }

    public Integer getPort() {
        return port;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    public String getFailedUrl() {
        return failedUrl;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }
}
