package org.example.webshopbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MerchantRequest {
    private Long merchantId;
    private String merchantPassword;
    private double amount;
    private String currency;
    private String merchantOrderId;
    private Date merchantTimestamp;

}
