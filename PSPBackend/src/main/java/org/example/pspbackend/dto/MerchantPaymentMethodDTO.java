package org.example.pspbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.pspbackend.domain.MerchantPaymentMethod;

@Getter
@Setter
public class MerchantPaymentMethodDTO {
    private Long merchantPaymentMethodId;
    private Boolean isEnabled;
    private Long paymentMethodId;

    public MerchantPaymentMethodDTO() {}

    public void setMerchantPaymentMethodId(Long merchantPaymentMethodId) {
        this.merchantPaymentMethodId = merchantPaymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Long getMerchantPaymentMethodId() {
        return merchantPaymentMethodId;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public MerchantPaymentMethodDTO(Long merchantPaymentMethodId, Boolean isEnabled, Long paymentMethodId) {
        this.merchantPaymentMethodId = merchantPaymentMethodId;
        this.isEnabled = isEnabled;
        this.paymentMethodId = paymentMethodId;
    }
}
