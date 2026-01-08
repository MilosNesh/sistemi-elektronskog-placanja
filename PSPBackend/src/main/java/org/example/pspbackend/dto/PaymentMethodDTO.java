package org.example.pspbackend.dto;

import lombok.*;
import org.example.pspbackend.domain.PaymentMethod;

@Getter
@Setter
@NoArgsConstructor
public class PaymentMethodDTO {
    private Long paymentMethodId;
    private String type;
    private String image;
    private String description;

    public PaymentMethodDTO() {}

    public PaymentMethodDTO(Long paymentMethodId, String type, String image, String description) {
        this.paymentMethodId = paymentMethodId;
        this.type = type;
        this.image = image;
        this.description = description;
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
}
