package org.example.pspbackend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantPaymentMethodDTO {
    private Long id;
    private String paymentMethod;
    private Boolean isEnabled;
}
