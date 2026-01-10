package org.example.pspbackend.service.impl;

import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.domain.Transaction;
import org.example.pspbackend.dto.MerchantRequest;
import org.example.pspbackend.dto.PaymentMethodDTO;
import org.example.pspbackend.repository.MerchantRepository;
import org.example.pspbackend.dto.PaymentResponse;
import org.example.pspbackend.repository.TransactionRepository;
import org.example.pspbackend.service.CallMerchantApiService;
import org.example.pspbackend.service.PaymentProviderService;
import org.example.pspbackend.service.PaymentService;
import org.example.pspbackend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private DynamicPaymentProviderRegistryImpl registry;
    @Autowired
    private CallMerchantApiService callMerchantApiService;
    @Autowired
    private MerchantServiceImpl merchantService;

    private final List<PaymentProviderService> providers;

    public PaymentServiceImpl(List<PaymentProviderService> providers) {
        this.providers = providers;
    }

    public String generatePaymentUrl(MerchantRequest merchantRequest){
        if(merchantRequest.getMerchantId() == null){
            return null;
        }

        Merchant merchant = merchantService.getById(merchantRequest.getMerchantId());
        if(merchant == null || !merchantRequest.getMerchantPassword().equals(merchant.getMerchantPassword())){
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
        transactionService.save(transaction);

        return "https://localhost:4200/payment/" + transaction.getId() + "/" + merchant.getMerchantId();
    }

    public String executePayment(String transactionId, PaymentMethodDTO request) {
        PaymentProviderService provider = registry.getProvider(request.getType());

        return provider.processPayment(transactionId, request);
    }

    public void sendPaymentStatusToMerchant(PaymentResponse paymentResponse){
        System.out.println("Prvi:  " + paymentResponse.getPspTimestamp());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date pspTimestamp;
        try {
            pspTimestamp = sdf.parse(paymentResponse.getPspTimestamp());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Nevalidan format pspTimestamp-a: " + paymentResponse.getPspTimestamp(), e);
        }

        Date acauirerTimestamp;
        try {
            acauirerTimestamp = sdf.parse(paymentResponse.getPspTimestamp());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Nevalidan format acquirerTimestamp-a: " + paymentResponse.getPspTimestamp(), e);
        }
        Transaction transaction = transactionService.get(paymentResponse.getStan(), paymentResponse.getMerchantId(), pspTimestamp);

        System.out.println("Drugi: " + pspTimestamp);
        if(transaction == null){
            return;
        }

        transaction.setGlobalTransactionId(paymentResponse.getGlobalTransactionId());
        transaction.setAcquirerTimestamp(acauirerTimestamp);
        transaction.setStatus(paymentResponse.getStatus());
        transactionService.save(transaction);

        Merchant merchant = merchantService.getById(paymentResponse.getMerchantId());
        if(merchant == null){
            return;
        }

        switch (paymentResponse.getStatus()){
            case "COMPLETED":
                callMerchantApiService.notifyPaymentSuccess(merchant.getSuccessUrl(), transaction.getMerchantOrderId());
                break;
            case "FAILED":
                callMerchantApiService.notifyPaymentFailed(merchant.getFailedUrl(), transaction.getMerchantOrderId());
                break;
            case "EXPIRED":
                callMerchantApiService.notifyPaymentError(merchant.getErrorUrl(), transaction.getMerchantOrderId());
                break;
            default:
                callMerchantApiService.notifyPaymentError(merchant.getErrorUrl(), transaction.getMerchantOrderId());
        }
    }

}
