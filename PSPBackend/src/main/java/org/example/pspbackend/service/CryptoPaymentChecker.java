package org.example.pspbackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.pspbackend.domain.CryptoPayment;
import org.example.pspbackend.repository.CryptoPaymentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CryptoPaymentChecker {
    private final CryptoPaymentRepository paymentRepository;
    private final BlockstreamClient blockstreamClient;

    public CryptoPaymentChecker(CryptoPaymentRepository paymentRepository, BlockstreamClient blockstreamClient) {
        this.paymentRepository = paymentRepository;
        this.blockstreamClient = blockstreamClient;
    }

    @Scheduled(fixedDelay = 10000) //svakih 10 sekundi
    public void checkPendingPayments(){
        List<CryptoPayment> pending = paymentRepository.findByStatus("PENDING");

        for(CryptoPayment payment : pending){
            JsonNode addressInfo = blockstreamClient.getAddressInfo(payment.getBtcAddress());

            JsonNode txs = addressInfo.get("chain_stats").get("tx_count");
            BigDecimal fundedSum = new BigDecimal(addressInfo.get("chain_stats").get("funded_txo_sum").asText());

            if (fundedSum.compareTo(payment.getBtcAmount().multiply(BigDecimal.valueOf(1e8))) >= 0){
                // tx primljen - update status
                payment.setStatus("SUCCESS");
                payment.setTxHash("placeholder-tx-hash");
                paymentRepository.save(payment);

                System.out.println("Payment successful: " + payment.getId());
            }
        }
    }
}
