package org.example.pspbackend.dto;

import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.domain.Role;

import java.util.List;

public class RegisterMerchantDTO {
    private Long merchantId;
    private String merchantEmail;
    private String sellerUrl;
    private List<MerchantPaymentMethodDTO> merchantPaymentMethods;
    private Role role;
    private String password;

    public RegisterMerchantDTO() {}

    public RegisterMerchantDTO(Merchant merchant) {
        this.merchantId = merchant.getMerchantId();
        this.merchantEmail = merchant.getMerchantEmail();
        this.sellerUrl = merchant.getSellerUrl();
        this.role = merchant.getRole();

        this.merchantPaymentMethods = merchant.getMerchantPaymentMethods()
                .stream()
                .map(mpm -> new MerchantPaymentMethodDTO(
                        mpm.getMerchantPaymentMethodId(),
                        mpm.getEnabled(),
                        mpm.getPaymentMethod().getPaymentMethodId()
                ))
                .toList();
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public String getMerchantEmail() {
        return merchantEmail;
    }

    public String getSellerUrl() {
        return sellerUrl;
    }

    public List<MerchantPaymentMethodDTO> getMerchantPaymentMethods() {
        return merchantPaymentMethods;
    }

    public Role getRole() { return role; }

    public String getPassword() { return password; }
}
