package org.example.pspbackend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ETHPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentId;

    private BigDecimal fiatAmount; //RSD
    private BigDecimal ethAmount;

    private String fromAddress;
    private String toAddress;

    private String txHash;
    private String status;

    private LocalDateTime createdAt;
}
