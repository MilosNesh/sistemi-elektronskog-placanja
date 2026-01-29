package org.example.pspbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private String id;
    private Long merchantId;
    private double amount;
    private String currency;
    private LocalDateTime merchantTimestamp;
    private String merchantOrderId;
    private LocalDateTime pspTimestamp;

}
