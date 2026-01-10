package org.example.bankbackend.domain;

public record ParsedQrData (
        String currency,
        Double amount,
        String receiverAccount,
        String receiverName,
        Long paymentId
){
}
