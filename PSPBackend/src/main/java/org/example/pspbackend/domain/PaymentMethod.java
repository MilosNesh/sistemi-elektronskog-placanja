package org.example.pspbackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.pspbackend.dto.PaymentMethodDTO;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "payment_methods")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    @Column(name = "type", nullable = false, unique = true, length = 50)
    private String type; // npr. "CREDIT_CARD", "PAYPAL", "BANK_TRANSFER"

    @Column(name = "image")
    private String image;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    public PaymentMethod() {}

    public PaymentMethod(String type, String image, String description, Boolean isAvailable) {
        this.type = type;
        this.image = image;
        this.description = description;
        this.isAvailable = isAvailable;
    }

    public PaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        this.type = paymentMethodDTO.getType();
        this.image = paymentMethodDTO.getImage();
        this.description = paymentMethodDTO.getDescription();
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public String getType() {
        return type;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getIsAvailable() { return isAvailable; }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
