package org.example.pspbackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment_methods")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethodType paymentMethod;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public void setPaymentMethod(PaymentMethodType paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public PaymentMethodType getPaymentMethod() {
        return paymentMethod;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }
}
