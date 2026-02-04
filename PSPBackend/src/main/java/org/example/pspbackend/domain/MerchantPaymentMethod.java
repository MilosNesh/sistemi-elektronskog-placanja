package org.example.pspbackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Entity
@Table(name = "merchant_payment_method")
public class MerchantPaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_payment_method_id")
    private Long merchantPaymentMethodId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;

    public MerchantPaymentMethod() {}

    public MerchantPaymentMethod(Merchant merchant, PaymentMethod paymentMethod) {
        this.merchant = merchant;
        this.paymentMethod = paymentMethod;
        this.isEnabled = true;
    }

    public void setMerchantPaymentMethodId(Long merchantPaymentMethodId) {
        this.merchantPaymentMethodId = merchantPaymentMethodId;
    }

    public Long getMerchantPaymentMethodId() {
        return merchantPaymentMethodId;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
}
