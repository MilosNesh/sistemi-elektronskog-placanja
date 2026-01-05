package org.example.pspbackend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.pspbackend.dto.MerchantDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "merchants")
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_id")
    private Long merchantId;

    @OneToMany(
            mappedBy = "merchant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    @Column(name = "merchant_username",
            nullable = false,
            unique = true,
            length = 50)
    private String merchantUsername;

    @Column(nullable = false, name = "merchant_password")
    private String merchantPassword;

    @Column(name = "seller_url")
    private String sellerUrl;

    @Column(name = "port")
    private Integer port;

    @Column(name = "success_url")
    private String successUrl;

    @Column(name = "failed_url")
    private String failedUrl;

    @Column(name = "error_url")
    private String errorUrl;

    public Long getMerchantId() {
        return merchantId;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public String getMerchantUsername() {
        return merchantUsername;
    }

    public String getMerchantPassword() {
        return merchantPassword;
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

    public String getFailedUrl() {
        return failedUrl;
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    public Merchant(MerchantDTO merchantDTO) {
        this.merchantId = merchantDTO.getMerchantId();
        this.merchantUsername = merchantDTO.getMerchantUsername();
        this.paymentMethods = merchantDTO.getPaymentMethods();
        this.sellerUrl = merchantDTO.getSellerUrl();
        this.port = merchantDTO.getPort();
        this.successUrl = merchantDTO.getSuccessUrl();
        this.failedUrl = merchantDTO.getFailedUrl();
        this.errorUrl = merchantDTO.getErrorUrl();
    }
}
