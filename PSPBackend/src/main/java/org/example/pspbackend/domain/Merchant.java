package org.example.pspbackend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.pspbackend.dto.MerchantDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "merchants")
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_id")
    private Long merchantId;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "merchant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MerchantPaymentMethod> merchantPaymentMethods = new ArrayList<>();

    @Column(name = "merchant_email",
            nullable = false,
            unique = true,
            length = 50)
    private String merchantEmail;

    @Column(name = "merchant_password")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    public Merchant() {}

    public Merchant(MerchantDTO merchantDTO) {
        this.merchantId = merchantDTO.getMerchantId();
        this.merchantEmail = merchantDTO.getMerchantEmail();
        this.sellerUrl = merchantDTO.getSellerUrl();
        this.port = merchantDTO.getPort();
        this.successUrl = merchantDTO.getSuccessUrl();
        this.failedUrl = merchantDTO.getFailedUrl();
        this.errorUrl = merchantDTO.getErrorUrl();
        this.role = merchantDTO.getRole();
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantPaymentMethods(List<MerchantPaymentMethod> merchantPaymentMethods) {
        this.merchantPaymentMethods = merchantPaymentMethods;
    }

    public List<MerchantPaymentMethod> getMerchantPaymentMethods() {
        return merchantPaymentMethods;
    }

    public String getMerchantEmail() {
        return merchantEmail;
    }

    public String getMerchantPassword() {
        return merchantPassword;
    }

    public String getSellerUrl() {
        return sellerUrl;
    }

    public void setMerchantEmail(String merchantEmail) {
        this.merchantEmail = merchantEmail;
    }

    public void setMerchantPassword(String merchantPassword) {
        this.merchantPassword = merchantPassword;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setSellerUrl(String sellerUrl) {
        this.sellerUrl = sellerUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public void setFailedUrl(String failedUrl) {
        this.failedUrl = failedUrl;
    }

    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
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

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }
}
