package org.example.pspbackend.service.impl;

import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.domain.Transaction;
import org.example.pspbackend.dto.MerchantRequest;
import org.example.pspbackend.dto.PaymentMethodDTO;
import org.example.pspbackend.repository.TransactionRepository;
import org.example.pspbackend.service.PaymentProvider;
import org.example.pspbackend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private TransactionRepository transactionRepository;

    private final List<PaymentProvider> providers;

    public PaymentServiceImpl(List<PaymentProvider> providers) {
        this.providers = providers;
    }

    public String generatePaymentUrl(MerchantRequest merchantRequest){
        if(merchantRequest.getMerchantId() == null){
            return null;
        }

        Merchant merchant = new Merchant();
        if(merchantRequest.getMerchantId() == null || !merchantRequest.getMerchantId().equals(merchant.getMerchantPassword())){
            return null;
        }
        Date pspTimestamp = Date.from(Instant.now());

        Transaction transaction = new Transaction(
                merchant,
                merchantRequest.getAmount(),
                merchantRequest.getCurrency(),
                merchantRequest.getMerchantTimestamp(),
                merchantRequest.getMerchantOrderId(),
                pspTimestamp
        );
        transactionRepository.save(transaction);

        return "http://localhost:4200/payment/" + transaction.getId() + "/" + merchant.getMerchantId();
    }


    public String executePayment(String transactionId, PaymentMethodDTO request) {
        return providers.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Payment type not supported"))
                .executePayment(transactionId, request);
    }

}
