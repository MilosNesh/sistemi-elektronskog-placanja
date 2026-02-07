package org.example.pspbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class TransactionDTO {
    private String id;
    private Long merchantId;
    private double amount;
    private String currency;
    private LocalDateTime merchantTimestamp;
    private String merchantOrderId;
    private LocalDateTime pspTimestamp;

    public TransactionDTO() {}
    public TransactionDTO(String id, Long merchantId, double amount, String currency, LocalDateTime merchantTimestamp, String merchantOrderId, LocalDateTime pspTimestamp) {
        this.id = id;
        this.merchantId = merchantId;
        this.amount = amount;
        this.currency = currency;
        this.merchantTimestamp = merchantTimestamp;
        this.merchantOrderId = merchantOrderId;
        this.pspTimestamp = pspTimestamp;
    }
}
