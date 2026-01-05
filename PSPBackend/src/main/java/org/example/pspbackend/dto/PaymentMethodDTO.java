package org.example.pspbackend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDTO {
    private Long id;
    private String paymentMethod;
    private Boolean isEnabled;
}
