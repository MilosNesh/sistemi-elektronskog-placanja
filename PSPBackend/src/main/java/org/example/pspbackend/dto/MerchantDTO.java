package org.example.pspbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDTO {
    private Long merchantId;
    private String merchantUsername;
    private String sellerUrl;
    private Integer port;
    private String successUrl;
    private String failedUrl;
    private String errorUrl;
    private List<MerchantPaymentMethodDTO> paymentMethods;

}
