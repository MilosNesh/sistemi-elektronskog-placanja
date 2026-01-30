package org.example.pspbackend.dto;

public class CryptoPaymentStatusDTO {
    private String status;   // PENDING / SUCCESS / FAILED
    private String txHash;   // može null dok je pending

    public CryptoPaymentStatusDTO(String status, String txHash) {
        this.status = status;
        this.txHash = txHash;
    }

    public String getStatus() {
        return status;
    }

    public String getTxHash() {
        return txHash;
    }
}
