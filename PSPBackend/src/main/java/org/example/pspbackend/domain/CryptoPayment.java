package org.example.pspbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class CryptoPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String paymentId;
    private String btcAddress;
    private BigDecimal fiatAmount;
    private BigDecimal btcAmount;
    private String status; // PENDING, SUCCESS, FAILED
    private String txHash; // TX hash kad uplata stigne
    private LocalDateTime createdAt;
}
