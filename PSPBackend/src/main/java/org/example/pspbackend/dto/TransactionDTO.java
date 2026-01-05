package org.example.pspbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private String id;
    private Long merchantId;
    private String merchantUsername;
    private Integer amount;
    private String currency;
    private LocalDateTime merchantTimestamp;
    private String merchantOrderId;
    private LocalDateTime pspTimestamp;

}
