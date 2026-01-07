package org.example.bankbackend.service;

import org.example.bankbackend.domain.Merchant;
import org.example.bankbackend.domain.PaymentInitRequest;
import org.example.bankbackend.domain.PaymentInitResponse;
import org.example.bankbackend.repository.MerchantRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {
    private final MerchantRepository merchantRepository;

    public PaymentService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public PaymentInitResponse initPayment(PaymentInitRequest request){
        Merchant merchant = merchantRepository.findById(request.getMerchantId())
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found"));

        if(!Boolean.TRUE.equals(merchant.getActive())) {
            throw new IllegalStateException("Merchant is not active");
        }
        String paymentId = UUID.randomUUID().toString();
        return new PaymentInitResponse(paymentId, "https://bank-frontend/pay/" + paymentId);
    }
}
