package org.example.bankbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

public record ParsedQrData(
        String currency,
        double amount,
        String receiverAccount,
        String merchantName,
        String purpose
) {}

