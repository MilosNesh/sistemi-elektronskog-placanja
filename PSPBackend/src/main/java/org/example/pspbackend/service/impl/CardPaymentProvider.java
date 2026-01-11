package org.example.pspbackend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.pspbackend.component.HmacUtil;
import org.example.pspbackend.component.StanGenerator;
import org.example.pspbackend.domain.Transaction;
import org.example.pspbackend.dto.PaymentInitRequest;
import org.example.pspbackend.dto.PaymentInitResponse;
import org.example.pspbackend.dto.PaymentMethodDTO;
import org.example.pspbackend.service.PaymentProviderService;
import org.example.pspbackend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardPaymentProvider implements PaymentProviderService {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CallBankApiServiceImpl callBankApiService;
    @Autowired
    private StanGenerator stanGenerator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private HmacUtil hmacUtil;

    @Override
    public String getSupportedPaymentType() {
        return "CREDIT_CARD";
    }

    @Override
    public String processPayment(String transactionId, PaymentMethodDTO request) {
        Transaction transaction = transactionService.getById(transactionId);

        String stan = stanGenerator.generateStan();
        PaymentInitRequest paymentInitRequest = new PaymentInitRequest(
                transaction.getMerchant().getMerchantId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                stan,
                transaction.getPspTimestamp().toString()
        );

        String json;
        try {
            json = objectMapper.writeValueAsString(paymentInitRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON serialization failed", e);
        }

        String hmacData = hmacUtil.generateHmac(json);
        PaymentInitResponse paymentInitResponse = callBankApiService.createPaymentInitRequest(paymentInitRequest, hmacData);

        transaction.setPaymentId(paymentInitResponse.getPaymentId());
        transaction.setStan(stan);
        transactionService.save(transaction);

        return paymentInitResponse.getPaymentUrl();
    }
}
