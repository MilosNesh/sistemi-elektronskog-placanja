package org.example.pspbackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
