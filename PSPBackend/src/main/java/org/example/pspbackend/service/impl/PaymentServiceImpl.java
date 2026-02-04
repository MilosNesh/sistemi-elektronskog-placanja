package org.example.pspbackend.service.impl;

import org.example.pspbackend.component.StanGenerator;
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
import java.time.LocalDateTime;
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
    @Autowired
    private StanGenerator stanGenerator;
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

        LocalDateTime pspTimestamp = LocalDateTime.now();

        Transaction transaction = new Transaction(
                merchant,
                merchantRequest.getAmount(),
                merchantRequest.getCurrency(),
                merchantRequest.getMerchantTimestamp(),
                merchantRequest.getMerchantOrderId(),
                pspTimestamp,
                stanGenerator.generateStan()

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
        LocalDateTime pspTimestamp = LocalDateTime.parse(paymentResponse.getPspTimestamp());

        Transaction transaction = transactionService.getByStan(paymentResponse.getStan());
//        System.out.println("Iz baze: " + transaction1.getStan() + " | " + transaction1.getMerchant().getMerchantId() + " | " + transaction1.getPspTimestamp());
//        Transaction transaction = transactionService.get(paymentResponse.getStan(), paymentResponse.getMerchantId(), pspTimestamp);
        System.out.println("Iz responsa: " + paymentResponse.getStan() + " | " + paymentResponse.getMerchantId() + " | " + pspTimestamp);

        System.out.println("Drugi: " + pspTimestamp);
        if(transaction == null){
            return;
        }

        transaction.setGlobalTransactionId(paymentResponse.getGlobalTransactionId());
        transaction.setAcquirerTimestamp(paymentResponse.getAcquirerTimestamp());
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
