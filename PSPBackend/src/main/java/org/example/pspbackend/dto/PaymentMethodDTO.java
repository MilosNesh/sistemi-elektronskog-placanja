package org.example.pspbackend.dto;

import lombok.*;
import org.example.pspbackend.domain.PaymentMethod;

@Getter
@Setter
public class PaymentMethodDTO {
    private Long paymentMethodId;
    private String type;
    private String image;
    private String description;
    private Boolean isAvailable;

    public PaymentMethodDTO() {}

    public PaymentMethodDTO(Long paymentMethodId, String type, String image, String description, Boolean isAvailable) {
        this.paymentMethodId = paymentMethodId;
        this.type = type;
        this.image = image;
        this.description = description;
        this.isAvailable = isAvailable;
    }

    public PaymentMethodDTO(PaymentMethod paymentMethod) {
        this.paymentMethodId = paymentMethod.getPaymentMethodId();
        this.type = paymentMethod.getType();
        this.image = paymentMethod.getImage();
        this.description = paymentMethod.getDescription();
        this.isAvailable = paymentMethod.getIsAvailable();
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

    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
}
